package com.micro.service.knowledge_base_service.service;

import com.micro.service.knowledge_base_service.entity.SubsectionImage;
import com.micro.service.knowledge_base_service.entity.SubsectionVideo;
import com.micro.service.knowledge_base_service.mapper.ContentMapper;
import com.micro.service.knowledge_base_service.mapper.SubsectionImageMapper;
import com.micro.service.knowledge_base_service.mapper.SubsectionVideoMapper;
import com.micro.service.knowledge_base_service.vo.ContentWithSubsectionVO;
import com.micro.service.knowledge_base_service.vo.SubsectionContentVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// 内容服务实现，负责知识库内容的查询和多媒体内容整合
@Service
public class ContentServiceImpl implements ContentService {

    // 注入内容数据访问对象
    @Autowired
    private ContentMapper contentMapper;

    // 注入图片数据访问对象
    @Autowired
    private SubsectionImageMapper imageMapper;

    // 注入视频数据访问对象
    @Autowired
    private SubsectionVideoMapper videoMapper;

    // 获取指定章节、小节和子节的内容，包含文本、图片和视频
    @Override
    public SubsectionContentVO getSubsectionContent(String chapterId, String sectionId, String subsectionId) {
        SubsectionContentVO result = new SubsectionContentVO();

        // 获取文本内容和子章节名称
        Map<String, Object> contentData = contentMapper.findContentWithSubsectionName(chapterId, sectionId, subsectionId);
        if (contentData != null) {
            result.setTextContent((String) contentData.get("content"));
            result.setSubsectionName((String) contentData.get("subsection_name"));
        }

        // 获取图片内容，处理空值的情况
        List<SubsectionImage> imageList = imageMapper.findBySubsectionId(subsectionId);
        List<SubsectionContentVO.ImageItem> images = (imageList == null) ? List.of() :
                imageList.stream().map(img -> {
                    SubsectionContentVO.ImageItem item = new SubsectionContentVO.ImageItem();
                    item.setImageUrl(img.getImageUrl());
                    item.setDescription(img.getDescription());
                    return item;
                }).collect(Collectors.toList());
        result.setImages(images);

        // 获取视频内容，处理空值的情况
        List<SubsectionVideo> videoList = videoMapper.findBySubsectionId(subsectionId);
        List<SubsectionContentVO.VideoItem> videos = (videoList == null) ? List.of() :
                videoList.stream().map(video -> {
                    SubsectionContentVO.VideoItem item = new SubsectionContentVO.VideoItem();
                    item.setVideoUrl(video.getVideoUrl());
                    item.setPlatform(video.getPlatform());
                    item.setDescription(video.getDescription());
                    return item;
                }).collect(Collectors.toList());
        result.setVideos(videos);

        return result;
    }

    // 根据小节名称获取相关内容列表
    @Override
    public List<ContentWithSubsectionVO> getContentBySectionName(String sectionName) {
        // 调用 mapper 中返回多个结果的方法
        return contentMapper.findContentBySectionName(sectionName);
    }
}
