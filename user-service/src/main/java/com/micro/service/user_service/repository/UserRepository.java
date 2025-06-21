package com.micro.service.user_service.repository;

import com.micro.service.user_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// 用户数据访问接口，提供用户相关的数据库操作方法
public interface UserRepository extends JpaRepository<User,Long> {
    // 根据用户名查找用户列表
    List<User> findByUsername(String username);
    // 根据邮箱查找单个用户
    User findByEmail(String email);
}
