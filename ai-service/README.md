# 篮球知识智能问答系统

这是一个基于 LangChain 和文心一言 API 的篮球知识智能问答系统。系统使用本地向量数据库进行文档检索，并通过文心一言 API 生成回答。支持多轮对话，能够保持对话的连贯性。

## 功能特点

- 使用 LangChain 进行文档处理和向量检索
- 使用 Chroma 作为本地向量数据库
- 使用文心一言 API 进行回答生成
- 支持中文问答
- 支持多轮对话，保持对话上下文
- 提供命令行和 API 两种使用方式

## 安装步骤

1. 克隆项目到本地
2. 安装依赖：
```bash
pip install -r requirements.txt
```

3. 配置环境变量：
   - 复制 `.env.example` 文件为 `.env`
   - 在 `.env` 文件中填入您的文心一言 API 密钥：
     ```
     ERNIE_API_KEY=your_api_key_here
     ERNIE_SECRET_KEY=your_secret_key_here
     ```

## 使用方法

### 命令行方式

1. 运行程序：
```bash
python app.py
```

2. 在命令行中输入问题，系统会基于知识库内容进行回答
3. 输入"退出"结束对话

### API 方式

1. 启动 API 服务：
```bash
python api.py
```

2. API 接口说明：

   a. 问答接口
   - 请求方式：POST
   - 接口地址：`/api/ask`
   - 请求体：
     ```json
     {
         "question": "你的问题"
     }
     ```
   - 响应体：
     ```json
     {
         "answer": "回答内容",
         "status": "success"
     }
     ```

   b. 健康检查接口
   - 请求方式：GET
   - 接口地址：`/api/health`
   - 响应体：
     ```json
     {
         "status": "healthy"
     }
     ```

## 多轮对话功能

系统支持多轮对话，能够记住最近的对话历史，使回答更加连贯。例如：

```
问：什么是篮球？
答：篮球是一项团队运动...

问：它的规则是什么？
答：篮球的规则包括...

问：刚才说的规则中，得分方式有哪些？
答：根据之前的规则说明，得分方式包括...
```

## 注意事项

- 确保已正确配置文心一言 API 密钥
- 首次运行时会下载必要的模型文件，可能需要一些时间
- 向量数据库文件会保存在 `chroma_db` 目录下
- 系统默认保留最近 5 轮对话历史
- API 服务默认运行在 8000 端口

## 技术架构

- 前端：命令行界面 / RESTful API
- 后端：Python + FastAPI
- 核心组件：
  - LangChain 框架
  - Chroma 向量数据库
  - 文心一言 API
  - HuggingFace 文本向量化模型 