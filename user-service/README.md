# User Service

本模块是AI篮球教练助手系统的用户服务子系统，基于Spring Boot开发，负责用户注册、登录、信息管理、邮箱验证码、权限校验等功能。

## 主要功能
- 用户注册与登录（支持邮箱验证码验证）
- 用户信息管理（查找、修改密码等）
- 邮箱验证码发送与校验
- 权限校验与接口访问控制
- RESTful API 对外服务

## 技术栈
- Spring Boot
- Spring Data JPA
- Spring Security
- Redis（验证码缓存）
- JUnit + Mockito（单元测试）
- Maven
- Docker（可选，支持容器化部署）

## 目录结构
```
user-service/
├── src/
│   ├── main/
│   │   ├── java/com/micro/service/user_service/
│   │   │   ├── controller/   # 控制器层，REST API
│   │   │   ├── service/      # 业务逻辑层
│   │   │   ├── repository/   # 数据访问层
│   │   │   ├── entity/       # 实体类
│   │   │   ├── config/       # 配置类
│   │   │   └── ...
│   │   └── resources/        # 配置文件、邮件模板等
│   └── test/                 # 单元测试
├── pom.xml                   # Maven依赖管理
├── Dockerfile                # 容器化部署配置
└── README.md                 # 项目说明
```

## 启动方式
1. 配置数据库、Redis、邮箱等参数（`src/main/resources/application.yml` 或 `application.properties`）
2. 使用Maven打包并运行：
   ```bash
   mvn clean package
   java -jar target/user-service-*.jar
   ```
3. 或使用Docker容器化部署：
   ```bash
   docker build -t user-service .
   docker run -p 8081:8081 user-service
   ```

## 测试说明
- 单元测试位于 `src/test/java` 目录，使用 JUnit 和 Mockito 编写
- 运行所有测试：
  ```bash
  mvn test
  ```

## 相关说明
- 本模块为AI篮球教练助手系统的子模块，需配合知识库服务等其他模块共同运行
- 如需扩展功能或对接前端，请参考 controller 层接口

---
如有问题请联系项目维护者。 