package com.micro.service.user_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

// 邮件服务，负责发送邮箱验证码等邮件
@Service
public class EmailService {

    // 注入邮件发送器
    @Autowired
    private JavaMailSender mailSender;

    // 发送验证码邮件到指定邮箱
    public void sendVerificationCode(String to, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("【验证码】");
        message.setText("您的验证码是：" + code + "，有效期5分钟。请勿泄露给他人。");
        message.setTo(to);
        message.setFrom("2645779070@qq.com");

        mailSender.send(message);
    }
}
