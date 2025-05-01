from datetime import datetime
from db.mysql import SessionLocal
from models.qa_model import AI_Question

# 创建一个本地会话
db = SessionLocal()

user_id = 1

# 示例问题和答案数据
questions_data = [
    {"question": "火车票怎么买？", "answer": "可以在铁路售票官网或第三方购票平台购买。"},
    {"question": "如何办理登机牌？", "answer": "可以通过自助机或服务台办理登机牌。"}
]

# 插入数据
for data in questions_data:
    new_question = AI_Question(
        question=data["question"],
        answer=data["answer"],
        user_id=user_id,
        created_at=datetime.now()
    )
    db.add(new_question)

# 提交数据到数据库
db.commit()
# 关闭会话
db.close()
print("数据已插入成功！")
