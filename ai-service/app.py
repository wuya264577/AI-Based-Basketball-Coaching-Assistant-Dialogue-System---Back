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

# 禁用SSL警告
urllib3.disable_warnings(urllib3.exceptions.InsecureRequestWarning)

# 加载环境变量
load_dotenv()

class BasketballQA:
    def __init__(self):
        # 初始化文本分割器
        self.text_splitter = RecursiveCharacterTextSplitter(
            chunk_size=1500,  # 增加块大小
            chunk_overlap=100,  # 减少重叠
            length_function=len,
        )
        
        # 配置重试策略
        retry_strategy = Retry(
            total=3,  # 减少重试次数
            backoff_factor=1,  # 减少重试间隔
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
            k=3,  # 减少记忆轮数
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
        
    def query_ernie(self, question, context):
        """调用文心一言API"""
        try:
            url = "https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/chat/completions"
            
            headers = {
                "Content-Type": "application/json"
            }
            
            # 获取对话历史
            chat_history = self.memory.load_memory_variables({})["chat_history"]
            chat_history_str = "\n".join([f"问：{msg.content}" if msg.type == "human" else f"答：{msg.content}" for msg in chat_history])
            
            # 使用提示模板
            prompt = self.prompt_template.format(
                chat_history=chat_history_str,
                context=context,
                question=question
            )
            
            data = {
                "messages": [
                    {
                        "role": "user",
                        "content": prompt
                    }
                ]
            }
            
            # 获取访问令牌
            token_url = "https://aip.baidubce.com/oauth/2.0/token"
            token_params = {
                "grant_type": "client_credentials",
                "client_id": os.getenv("ERNIE_API_KEY"),
                "client_secret": os.getenv("ERNIE_SECRET_KEY")
            }
            
            # 设置超时和重试
            session = requests.Session()
            retry_strategy = Retry(
                total=1,  # 只重试一次
                backoff_factor=1,
                status_forcelist=[429, 500, 502, 503, 504]
            )
            adapter = HTTPAdapter(max_retries=retry_strategy)
            session.mount("https://", adapter)
            
            # 获取token
            token_response = session.post(
                token_url, 
                params=token_params, 
                verify=False,
                timeout=5  # 减少超时时间
            )
            access_token = token_response.json()["access_token"]
            
            # 调用文心一言API
            response = session.post(
                f"{url}?access_token={access_token}",
                headers=headers,
                json=data,
                verify=False,
                timeout=15  # 减少超时时间
            )
            
            if response.status_code == 200:
                result = response.json()["result"]
                # 保存对话历史
                self.memory.save_context({"input": question}, {"output": result})
                return result
            else:
                print(f"API返回错误状态码: {response.status_code}")
                return "抱歉，API调用失败，请稍后重试。"
                
        except requests.exceptions.Timeout:
            print("请求超时，直接调用文心一言")
            # 直接使用问题调用文心一言，不使用上下文
            try:
                # 创建新的session
                new_session = requests.Session()
                new_session.mount("https://", HTTPAdapter(max_retries=1))
                
                # 重新获取token
                token_response = new_session.post(
                    token_url,
                    params=token_params,
                    verify=False,
                    timeout=5  # 减少超时时间
                )
                new_access_token = token_response.json()["access_token"]
                
                # 直接使用问题调用API
                data = {
                    "messages": [
                        {
                            "role": "user",
                            "content": question
                        }
                    ]
                }
                response = new_session.post(
                    f"{url}?access_token={new_access_token}",
                    headers=headers,
                    json=data,
                    verify=False,
                    timeout=15  # 减少超时时间
                )
                if response.status_code == 200:
                    result = response.json()["result"]
                    # 保存对话历史
                    self.memory.save_context({"input": question}, {"output": result})
                    return result
                else:
                    return "抱歉，API调用失败，请稍后重试。"
            except Exception as e:
                print(f"直接调用文心一言时出错: {str(e)}")
                return "抱歉，系统出现错误，请稍后重试。"
                
        except requests.exceptions.ConnectionError as e:
            print(f"连接错误: {str(e)}")
            return "抱歉，网络连接出现问题，请稍后重试。"
            
        except Exception as e:
            print(f"调用API时出错: {str(e)}")
            return "抱歉，系统出现错误，请稍后重试。"
    
    def answer_question(self, question):
        """回答问题"""
        if not self.vectorstore:
            return "请先加载文档！"
        
        try:
            # 检索相关文档
            docs = self.vectorstore.similarity_search(question, k=2)  # 减少检索数量
            context = "\n".join([doc.page_content for doc in docs])
            
            # 调用文心一言生成回答
            answer = self.query_ernie(question, context)
            return answer
        except Exception as e:
            print(f"回答问题时出错: {str(e)}")
            return "抱歉，处理您的问题时出现错误，请稍后重试。"

def main():
    print("正在初始化篮球知识问答系统...")
    try:
        # 初始化问答系统
        qa_system = BasketballQA()
        
        # 加载文档
        qa_system.load_document("mybook.txt")
        
        print("\n篮球知识问答系统已启动！")
        print("您可以开始提问了，输入'退出'结束对话。")
        
        while True:
            question = input("\n请输入您的问题：")
            if question.lower() in ['退出', 'quit', 'exit']:
                break
                
            answer = qa_system.answer_question(question)
            print("\n回答：", answer)
    except Exception as e:
        print(f"系统运行出错: {str(e)}")
        print("请检查网络连接和API密钥是否正确配置。")

if __name__ == "__main__":
    main() 