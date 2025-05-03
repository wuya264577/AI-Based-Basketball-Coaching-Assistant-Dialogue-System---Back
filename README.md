# AI篮球教练助手对话系统

这是一个基于AI的篮球教练助手对话系统，旨在为篮球爱好者和教练提供智能化的篮球知识咨询和教学辅助服务。

## 项目介绍

本项目是一个基于微服务架构的篮球知识库系统，主要包含以下功能：

1. 篮球知识库管理
   - 知识分类管理
   - 知识内容管理
   - 视频教程管理
   - 图片资源管理

2. AI智能对话
   - 篮球知识问答
   - 个性化推荐
   - 历史记录管理
   - 用户反馈收集

## 技术栈

- 后端框架：Spring Boot 3.4.4
- 数据库：MySQL
- 缓存：Redis
- 安全框架：Spring Security
- 认证方式：JWT
- 数据库访问：Spring Data JPA
- 开发语言：Java 17

## 项目结构

```
knowledge-base-service/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/micro/service/knowledge_base_service/
│   │   │       ├── controller/     # 控制器层
│   │   │       ├── service/        # 服务层
│   │   │       ├── repository/     # 数据访问层
│   │   │       ├── entity/         # 实体类
│   │   │       └── config/         # 配置类
│   │   └── resources/
│   │       └── application.properties
│   └── test/                       # 测试代码
└── pom.xml                         # Maven配置文件
```

## API文档

### 1. 知识库管理API

#### 搜索知识
- 请求方式：GET
- 接口路径：/api/knowledge/search
- 请求参数：
  - query: 搜索关键词
- 返回：知识列表

#### 按类型搜索
- 请求方式：GET
- 接口路径：/api/knowledge/type/{type}
- 请求参数：
  - type: 知识类型
- 返回：知识列表

#### 按难度搜索
- 请求方式：GET
- 接口路径：/api/knowledge/level/{level}
- 请求参数：
  - level: 难度等级
- 返回：知识列表

#### 获取视频教程
- 请求方式：GET
- 接口路径：/api/knowledge/videos
- 返回：视频教程列表

### 2. AI对话API

#### 提问
- 请求方式：POST
- 接口路径：/api/ai/ask
- 请求体：
```json
{
    "question": "如何提高投篮命中率？",
    "userId": "user123"
}
```
- 返回：AI回答

#### 获取相关问题
- 请求方式：POST
- 接口路径：/api/ai/related-knowledge
- 请求体：
```json
{
    "question": "如何提高投篮命中率？"
}
```
- 返回：相关知识列表

#### 提交反馈
- 请求方式：POST
- 接口路径：/api/ai/feedback/{consultationId}
- 请求体：
```json
{
    "isSatisfied": true,
    "feedback": "回答很专业，很有帮助"
}
```
- 返回：更新后的咨询记录

#### 获取历史记录
- 请求方式：GET
- 接口路径：/api/ai/history/{userId}
- 返回：用户历史咨询记录

#### 获取热门问题
- 请求方式：GET
- 接口路径：/api/ai/popular-questions
- 返回：热门问题列表

#### 获取推荐问题
- 请求方式：GET
- 接口路径：/api/ai/recommended-questions/{userId}
- 返回：推荐问题列表

## 部署说明

1. 环境要求
   - JDK 17+
   - MySQL 8.0+
   - Redis 6.0+

2. 配置修改
   - 修改 `application.properties` 中的数据库配置
   - 配置 AI 服务的 URL 和 API 密钥

3. 启动服务
```bash
mvn spring-boot:run
```


## 贡献指南

1. Fork 本仓库
2. 创建您的特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交您的更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 打开一个 Pull Request

## 许可证

本项目采用 MIT 许可证 - 详情请参阅 [LICENSE](LICENSE) 文件
