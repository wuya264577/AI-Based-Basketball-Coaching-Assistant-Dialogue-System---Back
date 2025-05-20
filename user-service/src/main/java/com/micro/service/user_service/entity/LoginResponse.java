package com.micro.service.user_service.entity;

public class LoginResponse {
    private String token;
    private Long userId;

    public LoginResponse(String token, Long userId) {
        this.token = token;
        this.userId = userId;
    }


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public LoginResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
