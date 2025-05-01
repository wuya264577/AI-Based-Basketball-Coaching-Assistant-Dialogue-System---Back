from sqlalchemy import Column, Integer, Text, ForeignKey, DateTime, String, BigInteger, func
from sqlalchemy.orm import declarative_base, relationship

Base = declarative_base()

class User(Base):
    __tablename__ = 'user'

    user_id = Column(BigInteger, primary_key=True, autoincrement=True)
    avatar = Column(String(255))
    created_at = Column(DateTime)
    email = Column(String(255), unique=True)
    password = Column(String(255))
    role = Column(String(255))
    username = Column(String(255))

    questions = relationship("AI_Question", back_populates="user")


class AI_Question(Base):
    __tablename__ = 'ai_question'

    qa_id = Column(Integer, primary_key=True, autoincrement=True)
    question = Column(Text, nullable=False)
    answer = Column(Text, nullable=True)
    user_id = Column(BigInteger, ForeignKey('user.user_id'), nullable=False)
    created_at = Column(DateTime, server_default=func.now())

    user = relationship("User", back_populates="questions")
