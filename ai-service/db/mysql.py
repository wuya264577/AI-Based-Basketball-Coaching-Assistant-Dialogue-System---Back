from sqlalchemy import create_engine
from sqlalchemy.orm import sessionmaker

# MySQL 配置
MYSQL_HOST = 'localhost'
MYSQL_PORT = 3307
MYSQL_USER = 'root'
MYSQL_PASSWORD = 'root123'
MYSQL_DB = 'oa_system'

# 构建连接 URL
# DATABASE_URL = f"mysql+pymysql://{MYSQL_USER}:{MYSQL_PASSWORD}@{MYSQL_HOST}:{MYSQL_PORT}/{MYSQL_DB}?useUnicode=true&characterEncoding=utf-8&serverTimezone=UTC"
DATABASE_URL = f"mysql+pymysql://{MYSQL_USER}:{MYSQL_PASSWORD}@{MYSQL_HOST}:{MYSQL_PORT}/{MYSQL_DB}?charset=utf8mb4"
# 创建数据库引擎
engine = create_engine(DATABASE_URL, echo=True)

# 创建会话本地工厂
SessionLocal = sessionmaker(autocommit=False, autoflush=False, bind=engine)

