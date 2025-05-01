from sentence_transformers import SentenceTransformer
import faiss
import pickle
from db.mysql import SessionLocal
from models.qa_model import AI_Question

# 定义模型和文件保存路径
VECTOR_FILE = "vectors.pkl"
INDEX_FILE = "faiss_index.bin"
MODEL_NAME = 'all-MiniLM-L12-v2'
MODEL = SentenceTransformer(MODEL_NAME)

def build_index():
    # 从数据库中读取问题数据
    db = SessionLocal()
    questions = db.query(AI_Question).all()
    db.close()

    # 提取问题和答案
    corpus = [q.question for q in questions]
    answers = [q.answer for q in questions]

    # 将问题转化为向量
    vectors = MODEL.encode(corpus)

    # 构建 FAISS 索引
    dimension = vectors.shape[1]  # 向量维度
    index = faiss.IndexFlatL2(dimension)  # L2 距离算法
    index.add(vectors)

    # 保存索引和元数据
    with open(VECTOR_FILE, "wb") as f:
        pickle.dump((corpus, answers), f)
    faiss.write_index(index, INDEX_FILE)

    print(f"索引已基于 {len(corpus)} 条数据更新完成！")


if __name__ == "__main__":
    build_index()