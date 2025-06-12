package com.micro.service.knowledge_base_service.service;

import com.micro.service.knowledge_base_service.entity.SubsectionImage;
import com.micro.service.knowledge_base_service.entity.SubsectionVideo;
import com.micro.service.knowledge_base_service.mapper.ContentMapper;
import com.micro.service.knowledge_base_service.mapper.SubsectionImageMapper;
import com.micro.service.knowledge_base_service.mapper.SubsectionVideoMapper;
import com.micro.service.knowledge_base_service.vo.ContentWithSubsectionVO;
import com.micro.service.knowledge_base_service.vo.SubsectionContentVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * 内容服务测试类
 */
@ExtendWith({SpringExtension.class, MockitoExtension.class})
public class ContentServiceTest {

    @InjectMocks
    private ContentServiceImpl contentService;

    @Mock
    private ContentMapper contentMapper;

    @Mock
    private SubsectionImageMapper imageMapper;

    @Mock
    private SubsectionVideoMapper videoMapper;

    private Map<String, Object> mockContentData;
    private List<SubsectionImage> mockImageList;
    private List<SubsectionVideo> mockVideoList;

    @BeforeEach
    public void setUp() {
        // 初始化测试数据
        mockContentData = new HashMap<>();
        mockContentData.put("content", "测试内容");
        mockContentData.put("subsection_name", "测试小节");

        // 初始化图片数据
        SubsectionImage image = new SubsectionImage();
        image.setImageUrl("http://example.com/image.jpg");
        image.setDescription("测试图片描述");
        mockImageList = Collections.singletonList(image);

        // 初始化视频数据
        SubsectionVideo video = new SubsectionVideo();
        video.setVideoUrl("http://example.com/video.mp4");
        video.setPlatform("YouTube");
        video.setDescription("测试视频描述");
        mockVideoList = Collections.singletonList(video);
    }

    @Test
    @DisplayName("测试获取子节内容 - 正常情况")
    public void testGetSubsectionContent_Normal() {
        // 准备测试数据
        String chapterId = "1";
        String sectionId = "2";
        String subsectionId = "3";

        // 配置mock行为
        when(contentMapper.findContentWithSubsectionName(chapterId, sectionId, subsectionId))
            .thenReturn(mockContentData);
        when(imageMapper.findBySubsectionId(subsectionId)).thenReturn(mockImageList);
        when(videoMapper.findBySubsectionId(subsectionId)).thenReturn(mockVideoList);

        // 执行测试
        SubsectionContentVO result = contentService.getSubsectionContent(chapterId, sectionId, subsectionId);

        // 验证结果
        assertNotNull(result);
        assertEquals("测试内容", result.getTextContent());
        assertEquals("测试小节", result.getSubsectionName());
        assertNotNull(result.getImages());
        assertEquals(1, result.getImages().size());
        assertEquals("http://example.com/image.jpg", result.getImages().get(0).getImageUrl());
        assertNotNull(result.getVideos());
        assertEquals(1, result.getVideos().size());
        assertEquals("http://example.com/video.mp4", result.getVideos().get(0).getVideoUrl());

        // 验证mock方法调用
        verify(contentMapper).findContentWithSubsectionName(chapterId, sectionId, subsectionId);
        verify(imageMapper).findBySubsectionId(subsectionId);
        verify(videoMapper).findBySubsectionId(subsectionId);
    }

    @Test
    @DisplayName("测试根据小节名称获取内容 - 正常情况")
    public void testGetContentBySectionName_Normal() {
        // 准备测试数据
        String sectionName = "基础训练";
        
        // 创建测试数据
        ContentWithSubsectionVO content1 = new ContentWithSubsectionVO();
        content1.setSubsectionId("1");
        content1.setSubsectionName("训练1");
        content1.setContent("内容1");
        
        ContentWithSubsectionVO content2 = new ContentWithSubsectionVO();
        content2.setSubsectionId("2");
        content2.setSubsectionName("训练2");
        content2.setContent("内容2");
        
        List<ContentWithSubsectionVO> expectedResults = Arrays.asList(content1, content2);

        // 配置mock行为
        when(contentMapper.findContentBySectionName(sectionName)).thenReturn(expectedResults);

        // 执行测试
        List<ContentWithSubsectionVO> actualResults = contentService.getContentBySectionName(sectionName);

        // 验证结果
        assertNotNull(actualResults);
        assertEquals(2, actualResults.size());
        assertEquals("训练1", actualResults.get(0).getSubsectionName());
        assertEquals("内容1", actualResults.get(0).getContent());

        // 验证mock方法调用
        verify(contentMapper).findContentBySectionName(sectionName);
    }

    @Test
    @DisplayName("测试获取子节内容 - 内容不存在")
    public void testGetSubsectionContent_NotFound() {
        // 准备测试数据
        String chapterId = "1";
        String sectionId = "2";
        String subsectionId = "3";

        // 配置mock行为
        when(contentMapper.findContentWithSubsectionName(chapterId, sectionId, subsectionId))
            .thenReturn(null);
        when(imageMapper.findBySubsectionId(subsectionId)).thenReturn(Collections.emptyList());
        when(videoMapper.findBySubsectionId(subsectionId)).thenReturn(Collections.emptyList());

        // 执行测试
        SubsectionContentVO result = contentService.getSubsectionContent(chapterId, sectionId, subsectionId);

        // 验证结果
        assertNotNull(result);
        assertNull(result.getTextContent());
        assertNull(result.getSubsectionName());
        assertTrue(result.getImages().isEmpty());
        assertTrue(result.getVideos().isEmpty());

        // 验证mock方法调用
        verify(contentMapper).findContentWithSubsectionName(chapterId, sectionId, subsectionId);
        verify(imageMapper).findBySubsectionId(subsectionId);
        verify(videoMapper).findBySubsectionId(subsectionId);
    }

    @Test
    @DisplayName("测试根据小节名称获取内容 - 空结果")
    public void testGetContentBySectionName_Empty() {
        // 准备测试数据
        String sectionName = "不存在的章节";

        // 配置mock行为
        when(contentMapper.findContentBySectionName(sectionName)).thenReturn(Collections.emptyList());

        // 执行测试
        List<ContentWithSubsectionVO> actualResults = contentService.getContentBySectionName(sectionName);

        // 验证结果
        assertNotNull(actualResults);
        assertTrue(actualResults.isEmpty());

        // 验证mock方法调用
        verify(contentMapper).findContentBySectionName(sectionName);
    }

    @Test
    @DisplayName("测试获取子节内容 - 无效参数")
    public void testGetSubsectionContent_InvalidParams() {
        // 准备测试数据
        String chapterId = null;
        String sectionId = "";
        String subsectionId = "3";

        // 配置mock行为
        when(contentMapper.findContentWithSubsectionName(chapterId, sectionId, subsectionId))
            .thenReturn(null);
        when(imageMapper.findBySubsectionId(subsectionId)).thenReturn(Collections.emptyList());
        when(videoMapper.findBySubsectionId(subsectionId)).thenReturn(Collections.emptyList());

        // 执行测试
        SubsectionContentVO result = contentService.getSubsectionContent(chapterId, sectionId, subsectionId);

        // 验证结果
        assertNotNull(result);
        assertNull(result.getTextContent());
        assertNull(result.getSubsectionName());
        assertTrue(result.getImages().isEmpty());
        assertTrue(result.getVideos().isEmpty());

        // 验证mock方法调用
        verify(contentMapper).findContentWithSubsectionName(chapterId, sectionId, subsectionId);
        verify(imageMapper).findBySubsectionId(subsectionId);
        verify(videoMapper).findBySubsectionId(subsectionId);
    }
}