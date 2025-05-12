package com.micro.service.knowledge_base_service.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "basketball_knowledge")
public class BasketballKnowledge {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(name = "knowledge_type")
    private String knowledgeType; // 知识类型：基础理论、技术动作、战术策略、体能训练等

    @Column(name = "difficulty_level")
    private String difficultyLevel; // 难度等级：初级、中级、高级

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    private String source; // 来源：同济大学教材、网络资源等

    @Column(columnDefinition = "TEXT")
    private String keywords;

    @Column(name = "video_url")
    private String videoUrl; // 教学视频链接

    @Column(name = "image_urls")
    private String imageUrls; // 图片链接，多个用逗号分隔

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