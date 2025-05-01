from fastapi import FastAPI
from routers import qa

app = FastAPI()
app.include_router(qa.router)
#uvicorn main:app --reload
