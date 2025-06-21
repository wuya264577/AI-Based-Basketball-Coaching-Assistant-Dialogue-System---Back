package com.micro.service.knowledge_base_service;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// 知识库服务应用程序的入口类
// 负责启动整个Spring Boot应用和初始化相关配置
@SpringBootApplication
@MapperScan("com.micro.service.knowledge_base_service.mapper")
public class KnowledgeBaseServiceApplication {

	// 应用程序主入口方法，启动Spring Boot服务
	public static void main(String[] args) {
		SpringApplication.run(KnowledgeBaseServiceApplication.class, args);
	}

}
