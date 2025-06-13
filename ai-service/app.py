import os
from dotenv import load_dotenv
from langchain.text_splitter import RecursiveCharacterTextSplitter
from langchain_community.embeddings import HuggingFaceEmbeddings
from langchain_community.vectorstores import Chroma
from langchain.chains import RetrievalQA
from langchain.prompts import PromptTemplate
from langchain.memory import ConversationBufferWindowMemory
import requests
import json
import time
from requests.adapters import HTTPAdapter
from urllib3.util.retry import Retry
import urllib3
import warnings
import torch
from huggingface_hub import snapshot_download
from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
from typing import Optional, Dict, Any
import logging

# 禁用SSL警告
urllib3.disable_warnings(urllib3.exceptions.InsecureRequestWarning)

# 加载环境变量
load_dotenv()

# 配置日志
logging.basicConfig(
    level=logging.INFO,
    format='%(asctime)s %(levelname)s %(name)s %(message)s',
    handlers=[
        logging.FileHandler('logs/ai-service.log', encoding='utf-8'),
        logging.StreamHandler()
    ]
)
logger = logging.getLogger(__name__)

# 定义响应模型
class Answer(BaseModel):
    reasoning: str
    answer: str

    class Config:
        from_attributes = True

app = FastAPI()

@app.post("/ask")
async def ask_question(question: str) -> Dict[str, str]:
    try:
        qa_system = BasketballQA()
        qa_system.load_document("mybook.txt")
        result = qa_system.answer_question(question)
        return {
            "reasoning": result["reasoning"],
            "answer": result["answer"]
        }
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))

class BasketballQA:
    def __init__(self):
        # 初始化文本分割器
        self.text_splitter = RecursiveCharacterTextSplitter(
            chunk_size=1000,
            chunk_overlap=200,
            length_function=len,
        )
        
        # 配置重试策略
        retry_strategy = Retry(
            total=5,
            backoff_factor=2,
            status_forcelist=[429, 500, 502, 503, 504],
        )
        adapter = HTTPAdapter(max_retries=retry_strategy)
        session = requests.Session()
        session.mount("https://", adapter)
        session.mount("http://", adapter)
        
        # 设置模型缓存目录
        cache_dir = "./model_cache"
        os.makedirs(cache_dir, exist_ok=True)
        
        # 初始化嵌入模型
        try:
            print("正在加载文本向量化模型...")
            model_path = snapshot_download(
                repo_id="shibing624/text2vec-base-chinese",  # 使用中文模型
                cache_dir=cache_dir,
                resume_download=True,
                local_files_only=True
            )
            
            self.embeddings = HuggingFaceEmbeddings(
                model_name=model_path,
                model_kwargs={'device': 'cpu'},
                encode_kwargs={'normalize_embeddings': True},
                cache_folder=cache_dir
            )
            print("文本向量化模型加载完成！")
            
        except Exception as e:
            print(f"初始化模型时出错: {str(e)}")
            raise
        
        # 初始化向量数据库
        self.vectorstore = None
        
        # 初始化对话记忆
        self.memory = ConversationBufferWindowMemory(
            k=5,  # 保留最近5轮对话
            memory_key="chat_history",
            return_messages=True
        )
        
        # 初始化提示模板
        self.prompt_template = PromptTemplate(
            template="""你是一个专业的篮球知识问答助手。请基于以下上下文和对话历史回答问题。如果上下文中没有相关信息，请自行补充。

对话历史：
{chat_history}

上下文：{context}

问题：{question}

回答：""",
            input_variables=["chat_history", "context", "question"]
        )
        
        # 讯飞星火API配置
        self.api_key = "Bearer BpGFNOZKthJXdQyRalAg:raDFOpsWmfsEiussDfRj"  # 请替换为您的API密钥
        self.url = "https://spark-api-open.xf-yun.com/v2/chat/completions"
        
    def load_document(self, file_path):
        """加载文档并创建向量数据库"""
        try:
            print("正在加载文档...")
            with open(file_path, 'r', encoding='utf-8') as f:
                text = f.read()
            
            # 分割文本
            chunks = self.text_splitter.split_text(text)
            print(f"文档已分割为 {len(chunks)} 个文本块")
            
            # 创建向量数据库
            print("正在创建向量数据库...")
            self.vectorstore = Chroma.from_texts(
                chunks, 
                self.embeddings,
                persist_directory="./chroma_db"
            )
            print("向量数据库创建完成！")
            
        except Exception as e:
            print(f"加载文档时出错: {str(e)}")
            raise
        
    def query_spark(self, question, context=None):
        """调用讯飞星火API"""
        try:
            # 准备请求头
            headers = {
                'Authorization': self.api_key,
                'content-type': "application/json"
            }
            
            # 准备消息
            if context:  # 如果有上下文，使用完整提示
                chat_history = self.memory.load_memory_variables({})["chat_history"]
                chat_history_str = "\n".join([f"问：{msg.content}" if msg.type == "human" else f"答：{msg.content}" for msg in chat_history])
                prompt = self.prompt_template.format(
                    chat_history=chat_history_str,
                    context=context,
                    question=question
                )
                messages = [{"role": "user", "content": prompt}]
            else:  # 如果没有上下文，直接使用问题
                messages = [{"role": "user", "content": question}]
            
            # 准备请求体
            body = {
                "model": "x1",
                "user": "user_id",
                "messages": messages,
                "stream": True,
                "tools": [
                    {
                        "type": "web_search",
                        "web_search": {
                            "enable": True,
                            "search_mode": "deep"
                        }
                    }
                ]
            }
            
            # 发送请求
            response = requests.post(url=self.url, json=body, headers=headers, stream=True)
            full_response = ""
            reasoning_content = ""
            isFirstContent = True
            
            for chunks in response.iter_lines():
                if chunks and '[DONE]' not in str(chunks):
                    data_org = chunks[6:]
                    chunk = json.loads(data_org)
                    text = chunk['choices'][0]['delta']
                    
                    # 处理思维链内容
                    if 'reasoning_content' in text and text['reasoning_content']:
                        reasoning_content += text["reasoning_content"]
                        print(text["reasoning_content"], end="")
                    
                    # 处理最终结果
                    if 'content' in text and text['content']:
                        content = text["content"]
                        if isFirstContent:
                            print("\n*******************以上为思维链内容，模型回复内容如下********************\n")
                            isFirstContent = False
                        print(content, end="")
                        full_response += content
            
            # 保存对话历史
            self.memory.save_context({"input": question}, {"output": full_response})
            
            # 返回思考过程和最终结果
            return {
                "reasoning": reasoning_content,
                "answer": full_response
            }
            
        except Exception as e:
            print(f"调用API时出错: {str(e)}")
            return {
                "reasoning": "",
                "answer": "抱歉，系统出现错误，请稍后重试。"
            }
    
    def answer_question(self, question):
        """回答问题"""
        if not self.vectorstore:
            return {
                "reasoning": "",
                "answer": "请先加载文档！"
            }
        
        try:
            # 检索相关文档
            docs = self.vectorstore.similarity_search(question, k=3)
            context = "\n".join([doc.page_content for doc in docs])
            
            # 调用星火大模型生成回答
            return self.query_spark(question, context)
        except requests.exceptions.Timeout:
            print("向量检索超时，直接使用星火大模型回答...")
            # 直接调用星火大模型，不提供上下文
            try:
                return self.query_spark(question)
            except Exception as e:
                print(f"直接调用星火大模型时出错: {str(e)}")
                return {
                    "reasoning": "",
                    "answer": "抱歉，系统出现错误，请稍后重试。"
                }
                
        except Exception as e:
            print(f"回答问题时出错: {str(e)}")
            return {
                "reasoning": "",
                "answer": "抱歉，处理您的问题时出现错误，请稍后重试。"
            }

def main():
    logger.info("正在初始化篮球知识问答系统...")
    try:
        qa_system = BasketballQA()
        qa_system.load_document("mybook.txt")
        logger.info("篮球知识问答系统已启动！您可以开始提问了，输入'退出'结束对话。")
        while True:
            question = input("\n请输入您的问题：")
            if question.lower() in ['退出', 'quit', 'exit']:
                break
            result = qa_system.answer_question(question)
            logger.info(f"思考过程：{result['reasoning']}")
            logger.info(f"回答：{result['answer']}")
            print("\n思考过程：", result["reasoning"])
            print("\n回答：", result["answer"])
    except Exception as e:
        logger.error(f"系统运行出错: {str(e)}", exc_info=True)
        print("系统运行出错: {}".format(str(e)))
        print("请检查网络连接和API密钥是否正确配置。")

if __name__ == "__main__":
    main() 