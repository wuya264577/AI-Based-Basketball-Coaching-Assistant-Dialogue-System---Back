package com.micro.service.knowledge_base_service.entity;

import lombok.Data;
import jakarta.persistence.*;
;

@Data
@Entity
@Table(name = "subsection_video") // 指定数据库表名
public class SubsectionVideo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 自增主键
    private int id;

    @Column(name = "subsection_id") // 映射数据库中的字段
    private String subsectionId;

    @Column(name = "video_url")
    private String videoUrl;

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

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    @Column(name = "platform")
    private String platform;

    @Column(name = "description")
    private String description;
}
