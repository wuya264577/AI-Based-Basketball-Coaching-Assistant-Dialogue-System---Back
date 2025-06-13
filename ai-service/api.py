from fastapi import FastAPI, HTTPException
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel
from typing import Optional
from app import BasketballQA
import uvicorn
import logging

# 创建FastAPI应用
app = FastAPI(
    title="篮球知识问答系统",
    description="基于LangChain和文心一言的篮球知识问答API，支持多轮对话",
    version="1.0.0"
)

# 配置CORS
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],  # 允许所有来源，生产环境建议设置具体的域名
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# 初始化问答系统
qa_system = None

# 定义请求模型
class Question(BaseModel):
    question: str

# 定义响应模型 - 新增reasoning字段
class Answer(BaseModel):
    answer: str
    reasoning: Optional[str] = None  # 思考过程是可选的
    status: str

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

@app.on_event("startup")
async def startup_event():
    """服务启动时初始化问答系统"""
    global qa_system
    try:
        qa_system = BasketballQA()
        qa_system.load_document("mybook.txt")
        logger.info("问答系统初始化成功！")
    except Exception as e:
        logger.error(f"初始化失败: {str(e)}", exc_info=True)
        raise

@app.post("/api/ask", response_model=Answer)
async def ask_question(question: Question):
    """问答接口，支持多轮对话，返回答案和思考过程"""
    if not qa_system:
        logger.error("系统未初始化")
        raise HTTPException(status_code=500, detail="系统未初始化")
    
    try:
        # 调用问答系统
        response = qa_system.answer_question(question.question)
        
        # 调试打印
        logger.debug(f"原始响应: {response}")
        
        # 处理不同类型的返回结果
        if isinstance(response, dict):
            # 如果返回字典，提取reasoning和answer
            reasoning = response.get("reasoning", "")
            answer = response.get("answer", "")
            
            # 如果answer为空但reasoning有内容，则使用reasoning作为answer
            if not answer and reasoning:
                answer = reasoning.split("\n")[-1]  # 取最后一行作为答案
                reasoning = "\n".join(reasoning.split("\n")[:-1])  # 其余部分作为思考过程
            elif not reasoning and answer:
                reasoning = answer  # 如果没有思考过程，使用答案作为思考过程
        else:
            # 如果不是字典，全部作为answer
            answer = str(response)
            reasoning = answer
        
        return Answer(
            answer=answer,
            reasoning=reasoning if reasoning != answer else None,  # 如果相同就不重复返回
            status="success"
        )
    except Exception as e:
        logger.error(f"接口错误: {str(e)}", exc_info=True)
        raise HTTPException(status_code=500, detail=str(e))

@app.get("/api/health")
async def health_check():
    """健康检查接口"""
    return {"status": "healthy"}

if __name__ == "__main__":
    uvicorn.run("api:app", host="0.0.0.0", port=8000, reload=True)