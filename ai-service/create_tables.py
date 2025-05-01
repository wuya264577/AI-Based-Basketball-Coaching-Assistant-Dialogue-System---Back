from db.mysql import engine
from qa_model import Base

# 创建表
Base.metadata.create_all(bind=engine)
print("所有表已创建成功！")