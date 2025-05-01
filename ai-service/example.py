from datetime import datetime

from db.mysql import SessionLocal
from models.qa_model import AI_Question


# 创建一个本地会话
db = SessionLocal()

user_id = 1

# 示例问题和答案数据
questions_data = [
    {"question": "飞机起飞时间怎么查？", "answer": "您可以在航班动态页面输入航班号查询。"},
    {"question": "飞机上可以带水吗？", "answer": "过安检后可携带，安检前禁止携带液体。"}
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
