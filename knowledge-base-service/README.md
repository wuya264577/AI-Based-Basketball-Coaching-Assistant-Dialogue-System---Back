# Knowledge Base Service

本模块是AI篮球教练助手系统的知识库后端服务，基于Spring Boot开发，提供知识内容的管理、检索和多媒体支持。

## 主要功能
- 文章管理：增删查改、浏览量和下载量统计、热门/最新文章获取
- 知识内容管理：按章节、小节、子节组织内容，支持多媒体（图片、视频）
- 搜索功能：支持关键词模糊搜索
- RESTful API：对外提供标准接口，便于前端或其他服务调用

## 技术栈
- Spring Boot
- Spring Data JPA / MyBatis
- MySQL（或兼容数据库）
- JUnit + Mockito（单元测试）
- Maven
- Docker（可选，支持容器化部署）

## 目录结构
```
knowledge-base-service/
├── src/
│   ├── main/
│   │   ├── java/com/micro/service/knowledge_base_service/
│   │   │   ├── controller/   # 控制器层，REST API
│   │   │   ├── service/      # 业务逻辑层
│   │   │   ├── repository/   # 数据访问层
│   │   │   ├── entity/       # 实体类
│   │   │   ├── vo/           # 视图对象
│   │   │   ├── mapper/       # MyBatis映射
│   │   │   └── ...
│   │   └── resources/        # 配置文件、SQL脚本等
│   └── test/                 # 单元测试
├── pom.xml                   # Maven依赖管理
├── Dockerfile                # 容器化部署配置
└── README.md                 # 项目说明
```

## 启动方式
1. 配置数据库连接等参数（`src/main/resources/application.yml` 或 `application.properties`）
2. 使用Maven打包并运行：
   ```bash
   mvn clean package
   java -jar target/knowledge-base-service-*.jar
   ```
3. 或使用Docker容器化部署：
   ```bash
   docker build -t knowledge-base-service .
   docker run -p 8080:8080 knowledge-base-service
   ```

## 测试说明
- 单元测试位于 `src/test/java` 目录，使用 JUnit 和 Mockito 编写
- 运行所有测试：
  ```bash
  mvn test
  ```

## 相关说明
- 本模块为AI篮球教练助手系统的子模块，需配合用户服务等其他模块共同运行
- 如需扩展功能或对接前端，请参考 controller 层接口

---
如有问题请联系项目维护者。 