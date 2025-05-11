package com.micro.service.knowledge_base_service.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "subsection_image") // 指定数据库表名
public class SubsectionImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 自增主键
    private int id;

    @Column(name = "subsection_id") // 映射数据库中的字段
    private String subsectionId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSubsectionId() {
        return subsectionId;
    }

    public void setSubsectionId(String subsectionId) {
        this.subsectionId = subsectionId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "description")
    private String description;
}
