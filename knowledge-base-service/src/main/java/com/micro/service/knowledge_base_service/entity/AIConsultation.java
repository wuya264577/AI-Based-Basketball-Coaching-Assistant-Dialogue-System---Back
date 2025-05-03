package com.micro.service.knowledge_base_service.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "ai_consultation")
public class AIConsultation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String userId; // 咨询用户ID

    @Column(columnDefinition = "TEXT", nullable = false)
    private String question; // 用户问题

    @Column(columnDefinition = "TEXT")
    private String answer; // AI回答

    @Column(name = "knowledge_type")
    private String knowledgeType; // 问题类型：技术动作、战术策略、体能训练等

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "is_satisfied")
    private Boolean isSatisfied; // 用户是否满意

    @Column(name = "feedback")
    private String feedback; // 用户反馈

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
} 