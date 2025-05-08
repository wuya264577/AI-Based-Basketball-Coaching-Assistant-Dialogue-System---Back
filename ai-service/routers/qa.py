import json
from fastapi import Query
from fastapi import APIRouter
from pydantic import BaseModel
from sentence_transformers import SentenceTransformer
import faiss
import pickle
import uuid  # 用于生成 session_id
from db.redis import get_redis_connection

# 加载预定义的模型和索引
MODEL_NAME = 'all-MiniLM-L12-v2'
MODEL = SentenceTransformer(MODEL_NAME)
VECTOR_FILE = "vectors.pkl"
INDEX_FILE = "faiss_index.bin"

with open(VECTOR_FILE, "rb") as f:
    corpus, answers = pickle.load(f)
index = faiss.read_index(INDEX_FILE)

# 创建 Redis 连接
redis_conn = get_redis_connection()

# 定义 FastAPI 路由
router = APIRouter()


class QuestionRequest(BaseModel):
    session_id: str = None  # 会话 ID（可选）
    question: str  # 用户提问的问题


@router.post("/start_session")
def start_session():
    """
    创建一个新的会话 ID
    """
    session_id = str(uuid.uuid4())  # 生成唯一的 session_id
    return {"session_id": session_id}

@router.post("/qa")
def get_answer(req: QuestionRequest, max_history_length: int = Query(10)):
    """
    带智能上下文支持的多轮问答接口
    """
    if not req.session_id:
        session_id = str(uuid.uuid4())
        req.session_id = session_id
    else:
        session_id = req.session_id

    session_key = f"session:{session_id}:history"

    # 获取 Redis 中完整的对话历史
    history = redis_conn.lrange(session_key, 0, -1)
    conversation_history = [json.loads(item) for item in history]  # 转为 JSON 对象列表

    # 智能上下文生成
    relevant_contexts = []
    if conversation_history:
        for history_entry in conversation_history[-max_history_length:]:
            similarity = (
                MODEL.encode([history_entry["question"]])[0]
                @ MODEL.encode([req.question])[0]
            )
            if similarity > 0.7:  # 仅使用相关性高的上下文
                relevant_contexts.append(history_entry["question"])
        context_summary = " ".join(relevant_contexts)
    else:
        context_summary = ""

    # 如果有上下文，增强当前问题；否则使用当前问题
    all_text = f"{context_summary} {req.question}" if relevant_contexts else req.question

    # 语义搜索
    query_vector = MODEL.encode([all_text])
    top_k = 3
    distances, indices = index.search(query_vector, top_k)

    # 处理 FAISS 返回结果
    results = []
    for i in range(top_k):
        similarity = 1.0 - distances[0][i]
        results.append({
            "matched_question": corpus[indices[0][i]],
            "answer": answers[indices[0][i]],
            "similarity": float(similarity)
        })

    # 匹配结果处理
    if results:
        selected_answer = results[0]["answer"]

        # 存储本轮问题和答案
        redis_conn.rpush(
            session_key,
            json.dumps({"question": req.question, "answer": selected_answer})
        )
        redis_conn.ltrim(session_key, -max_history_length, -1)  # 保留最多 max_history_length 条记录
        redis_conn.expire(session_key, 900)  # 设置过期时间

        return {
            "user_question": req.question,
            "results": results,
            "history": conversation_history,  # 返回对话历史
            "session_id": session_id
        }

    # 无匹配结果逻辑
    redis_conn.rpush(
        session_key,
        json.dumps({"question": req.question, "answer": "对不起，我暂时不知道这个问题的答案。"})
    )
    redis_conn.ltrim(session_key, -max_history_length, -1)
    redis_conn.expire(session_key, 900)

    return {
        "user_question": req.question,
        "answer": "对不起，我暂时不知道这个问题的答案。",
        "history": conversation_history,
        "session_id": session_id
    }


@router.get("/monitor_sessions")
def monitor_sessions():
    """
    Redis 缓存统计和监控
    检查当前活跃会话及其过期时间
    """
    keys = redis_conn.keys("session:*:history")  # 获取所有会话键
    active_sessions = []

    for key in keys:
        ttl = redis_conn.ttl(key)  # 获取剩余存活时间
        active_sessions.append({
            "session_key": key,
            "time_remaining": ttl  # 剩余时间（秒）
        })

    return {
        "active_sessions": active_sessions,
        "total_sessions": len(active_sessions)
    }