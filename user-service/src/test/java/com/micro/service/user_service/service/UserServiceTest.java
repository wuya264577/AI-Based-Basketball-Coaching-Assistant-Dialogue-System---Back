package com.micro.service.user_service.service;

import com.micro.service.user_service.entity.User;
import com.micro.service.user_service.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * 用户服务测试类
 */
@ExtendWith({SpringExtension.class, MockitoExtension.class})
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    private User mockUser;

    @BeforeEach
    public void setUp() {
        // 初始化测试数据
        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("testuser");
        mockUser.setPassword("password123");
        mockUser.setEmail("test@example.com");
        mockUser.setRole("USER");
        mockUser.setAvatar("avatar.jpg");
        mockUser.setCreatedAt(LocalDateTime.now());
    }

    @Test
    @DisplayName("测试用户注册 - 正常情况")
    public void testRegisterUser_Normal() {
        // 配置mock行为
        when(userRepository.save(any(User.class))).thenReturn(mockUser);

        // 执行测试
        User result = userService.registerUser(mockUser);

        // 验证结果
        assertNotNull(result);
        assertEquals(mockUser.getUsername(), result.getUsername());
        assertEquals(mockUser.getEmail(), result.getEmail());
        assertEquals(mockUser.getRole(), result.getRole());
        assertNotNull(result.getCreatedAt());

        // 验证mock方法调用
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("测试根据用户名查找用户 - 正常情况")
    public void testFindByUsername_Normal() {
        // 准备测试数据
        List<User> expectedUsers = Arrays.asList(mockUser);

        // 配置mock行为
        when(userRepository.findByUsername("testuser")).thenReturn(expectedUsers);

        // 执行测试
        List<User> results = userService.findByUsername("testuser");

        // 验证结果
        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals(mockUser.getUsername(), results.get(0).getUsername());
        assertEquals(mockUser.getEmail(), results.get(0).getEmail());

        // 验证mock方法调用
        verify(userRepository).findByUsername("testuser");
    }

    @Test
    @DisplayName("测试根据邮箱查找用户 - 正常情况")
    public void testFindByEmail_Normal() {
        // 配置mock行为
        when(userRepository.findByEmail("test@example.com")).thenReturn(mockUser);

        // 执行测试
        User result = userService.findByEmail("test@example.com");

        // 验证结果
        assertNotNull(result);
        assertEquals(mockUser.getUsername(), result.getUsername());
        assertEquals(mockUser.getEmail(), result.getEmail());

        // 验证mock方法调用
        verify(userRepository).findByEmail("test@example.com");
    }

    @Test
    @DisplayName("测试更新用户密码 - 正常情况")
    public void testUpdatePassword_Normal() {
        // 准备测试数据
        String newPassword = "newpassword123";
        mockUser.setPassword(newPassword);

        // 配置mock行为
        when(userRepository.save(any(User.class))).thenReturn(mockUser);

        // 执行测试
        userService.updatePassword(mockUser);

        // 验证mock方法调用
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("测试用户注册 - 邮箱已存在")
    public void testRegisterUser_DuplicateEmail() {
        // 配置mock行为
        when(userRepository.findByEmail("test@example.com")).thenReturn(mockUser);
        when(userRepository.save(any(User.class))).thenReturn(mockUser);

        // 执行测试
        User result = userService.registerUser(mockUser);

        // 验证结果
        assertNotNull(result);
        assertEquals(mockUser.getEmail(), result.getEmail());

        // 验证mock方法调用
        //verify(userRepository).findByEmail("test@example.com");
        verify(userRepository).save(any(User.class));
    }
}