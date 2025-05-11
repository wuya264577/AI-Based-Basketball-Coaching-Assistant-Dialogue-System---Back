package com.micro.service.knowledge_base_service.service;

import com.micro.service.knowledge_base_service.entity.SubsectionImage;
import com.micro.service.knowledge_base_service.entity.SubsectionVideo;
import com.micro.service.knowledge_base_service.mapper.ContentMapper;
import com.micro.service.knowledge_base_service.mapper.SubsectionImageMapper;
import com.micro.service.knowledge_base_service.mapper.SubsectionVideoMapper;
import com.micro.service.knowledge_base_service.vo.SubsectionContentVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContentServiceImpl implements ContentService {

    @Autowired
    private ContentMapper contentMapper;

    @Autowired
    private SubsectionImageMapper imageMapper;

    @Autowired
    private SubsectionVideoMapper videoMapper;

    @Override
    public SubsectionContentVO getSubsectionContent(String chapterId, String sectionId, String subsectionId) {
        SubsectionContentVO result = new SubsectionContentVO();

        // 获取文本内容
        String text = contentMapper.findContent(chapterId, sectionId, subsectionId);
        result.setTextContent(text);


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
}
