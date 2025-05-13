package com.micro.service.knowledge_base_service.vo;

import lombok.Data;

import java.util.List;

@Data
public class SubsectionContentVO {
    private String textContent;
    private List<ImageItem> images;
    private List<VideoItem> videos;
    private String subsectionName;

    public String getSubsectionName() {
        return subsectionName;
    }

    public void setSubsectionName(String subsectionName) {
        this.subsectionName = subsectionName;
    }

    public String getTextContent() {
        return textContent;
    }

    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }

    public List<ImageItem> getImages() {
        return images;
    }

    public void setImages(List<ImageItem> images) {
        this.images = images;
    }

    public List<VideoItem> getVideos() {
        return videos;
    }

    public void setVideos(List<VideoItem> videos) {
        this.videos = videos;
    }

    @Data
    public static class ImageItem {
        private String imageUrl;

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

        private String description;
    }

    @Data
    public static class VideoItem {
        private String videoUrl;
        private String platform;

        public String getVideoUrl() {
            return videoUrl;
        }

        public void setVideoUrl(String videoUrl) {
            this.videoUrl = videoUrl;
        }

        public String getPlatform() {
            return platform;
        }

        public void setPlatform(String platform) {
            this.platform = platform;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        private String description;
    }
}
