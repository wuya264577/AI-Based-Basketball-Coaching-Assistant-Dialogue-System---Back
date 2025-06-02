package com.micro.service.user_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 用户服务应用程序入口类
 * 
 * SpringBootApplication 注解表明这是一个Spring Boot应用程序，它包含了以下功能：
 * 1. @Configuration：标记该类为配置类
 * 2. @EnableAutoConfiguration：启用Spring Boot的自动配置机制
 * 3. @ComponentScan：启用组件扫描，自动发现和注册Spring组件
 */
@SpringBootApplication
public class UserServiceApplication {

	/**
	 * 应用程序主入口方法
	 * 负责启动Spring Boot应用程序
	 * 
	 * param args 命令行参数，可用于配置应用程序的启动参数
	 */
	public static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
	}

}
