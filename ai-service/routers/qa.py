from fastapi import APIRouter
from pydantic import BaseModel
from sentence_transformers import SentenceTransformer
import faiss
import pickle

# 加载预定义的模型和索引
MODEL_NAME = 'all-MiniLM-L12-v2'
MODEL = SentenceTransformer(MODEL_NAME)
VECTOR_FILE = "vectors.pkl"
INDEX_FILE = "faiss_index.bin"

with open(VECTOR_FILE, "rb") as f:
    corpus, answers = pickle.load(f)
index = faiss.read_index(INDEX_FILE)

router = APIRouter()

class QuestionRequest(BaseModel):
    question: str

@router.post("/qa")
def get_answer(req: QuestionRequest):
    query_vector = MODEL.encode([req.question])
    top_k = 3  # 返回三个最相似的
    distances, indices = index.search(query_vector, top_k)

    results = []
    for i in range(top_k):
        if distances[0][i] < 0.5:  # 匹配阈值
            # 将 distances[0][i] 转换为 float 类型
            results.append({
                "matched_question": corpus[indices[0][i]],
                "answer": answers[indices[0][i]],
                "distance": float(distances[0][i])  # 转换为原生 float 类型
            })

    if results:
        return {
            "user_question": req.question,
            "results": results
        }
    else:
        return {
            "user_question": req.question,
            "answer": "对不起，我暂时不知道这个问题的答案。"
        }