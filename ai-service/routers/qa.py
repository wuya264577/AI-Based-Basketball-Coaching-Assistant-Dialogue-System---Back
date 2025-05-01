from fastapi import APIRouter
from db.mysql import SessionLocal
from pydantic import BaseModel
from models.qa_model import AI_Question

router = APIRouter()


class QuestionRequest(BaseModel):
    question: str


@router.post("/qa")
def get_answer(req: QuestionRequest):
    db = SessionLocal()
    q = req.question

    # 使用 SQLAlchemy ORM 查询 AI_Question
    result = db.query(AI_Question).filter(AI_Question.question.like(f"%{q}%")).first()  # 查找第一个包含关键词的问题

    if result:
        return {"answer": result.answer}
    return {"answer": "对不起，我暂时不知道这个问题的答案。"}
