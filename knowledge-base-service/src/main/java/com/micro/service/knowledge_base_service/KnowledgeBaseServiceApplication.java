package com.micro.service.knowledge_base_service;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@MapperScan("com.micro.service.knowledge_base_service.mapper")
public class KnowledgeBaseServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(KnowledgeBaseServiceApplication.class, args);
	}

}
