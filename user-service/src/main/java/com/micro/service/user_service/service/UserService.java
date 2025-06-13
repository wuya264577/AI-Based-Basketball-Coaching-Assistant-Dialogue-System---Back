package com.micro.service.user_service.service;

import com.micro.service.user_service.entity.User;
import com.micro.service.user_service.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    public User registerUser(User user){
        User saved = userRepository.save(user);
        logger.info("注册用户: {}", user.getEmail());
        return saved;
    }

    public List<User> findByUsername(String username){
        logger.debug("查找用户名: {}", username);
        return userRepository.findByUsername(username);
    }

    public User findByEmail(String email) {
        logger.debug("查找邮箱: {}", email);
        return userRepository.findByEmail(email);  // 返回单个用户对象
    }

    public void updatePassword(User user) {
        userRepository.save(user);
        logger.info("更新用户密码: {}", user.getEmail());
    }
}
