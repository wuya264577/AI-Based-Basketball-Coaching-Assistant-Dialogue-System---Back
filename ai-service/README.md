# 篮球知识智能问答系统

这是一个基于 LangChain 和文心一言 API 的篮球知识智能问答系统。系统使用本地向量数据库进行文档检索，并通过文心一言 API 生成回答。

## 功能特点

- 使用 LangChain 进行文档处理和向量检索
- 使用 Chroma 作为本地向量数据库
- 使用文心一言 API 进行回答生成
- 支持中文问答

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

1. 运行程序：
```bash
python app.py
```

2. 在命令行中输入问题，系统会基于知识库内容进行回答
3. 输入"退出"结束对话

## 注意事项

- 确保已正确配置文心一言 API 密钥
- 首次运行时会下载必要的模型文件，可能需要一些时间
- 向量数据库文件会保存在 `chroma_db` 目录下 