from fastapi import FastAPI, HTTPException
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel
from app import BasketballQA
import uvicorn

# 创建FastAPI应用
app = FastAPI(
    title="篮球知识问答系统",
    description="基于LangChain和文心一言的篮球知识问答API",
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

# 定义响应模型
class Answer(BaseModel):
    answer: str
    status: str

@app.on_event("startup")
async def startup_event():
    """服务启动时初始化问答系统"""
    global qa_system
    try:
        qa_system = BasketballQA()
        qa_system.load_document("mybook.txt")
    except Exception as e:
        print(f"初始化失败: {str(e)}")
        raise

@app.post("/api/ask", response_model=Answer)
async def ask_question(question: Question):
    """问答接口"""
    if not qa_system:
        raise HTTPException(status_code=500, detail="系统未初始化")
    
    try:
        answer = qa_system.answer_question(question.question)
        return Answer(answer=answer, status="success")
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))

@app.get("/api/health")
async def health_check():
    """健康检查接口"""
    return {"status": "healthy"}

if __name__ == "__main__":
    uvicorn.run("api:app", host="0.0.0.0", port=8000, reload=True) 