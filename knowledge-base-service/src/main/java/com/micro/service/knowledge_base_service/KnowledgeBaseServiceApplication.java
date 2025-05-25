package com.micro.service.knowledge_base_service;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 知识库服务应用程序入口类
 * 
 * @SpringBootApplication 注解表明这是一个Spring Boot应用程序
 * @MapperScan 注解用于扫描指定包下的MyBatis Mapper接口
 */
@SpringBootApplication
@MapperScan("com.micro.service.knowledge_base_service.mapper")
public class KnowledgeBaseServiceApplication {

	/**
	 * 应用程序主入口方法
	 * 
	 * @param args 命令行参数
	 */
	public static void main(String[] args) {
		SpringApplication.run(KnowledgeBaseServiceApplication.class, args);
	}

}
