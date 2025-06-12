package com.micro.service.knowledge_base_service.service;

import com.micro.service.knowledge_base_service.mapper.SearchMapper;
import com.micro.service.knowledge_base_service.vo.SearchResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * 搜索服务测试类
 */
@ExtendWith({SpringExtension.class, MockitoExtension.class})
public class SearchServiceTest {

    @InjectMocks
    private SearchServiceImpl searchService;

    @Mock
    private SearchMapper searchMapper;

    private SearchResult mockSearchResult;

    @BeforeEach
    public void setUp() {
        // 初始化测试数据
        mockSearchResult = new SearchResult();
        mockSearchResult.setSubsectionId("1");
        mockSearchResult.setChapterName("篮球基础");
        mockSearchResult.setSectionName("基本规则");
        mockSearchResult.setSubsectionName("比赛规则");
        mockSearchResult.setContent("篮球比赛基本规则介绍");
    }

    @Test
    @DisplayName("测试搜索功能 - 正常关键词")
    public void testSearchByKeyword_Normal() {
        // 准备测试数据
        String keyword = "篮球";
        List<SearchResult> expectedResults = Collections.singletonList(mockSearchResult);

        // 配置mock行为
        when(searchMapper.searchByKeyword("%" + keyword + "%")).thenReturn(expectedResults);

        // 执行测试
        List<SearchResult> actualResults = searchService.searchByKeyword(keyword);

        // 验证结果
        assertNotNull(actualResults);
        assertEquals(1, actualResults.size());
        assertEquals(mockSearchResult.getChapterName(), actualResults.get(0).getChapterName());
        assertEquals(mockSearchResult.getContent(), actualResults.get(0).getContent());
        
        // 验证mock方法被调用
        verify(searchMapper, times(1)).searchByKeyword("%" + keyword + "%");
    }

    @Test
    @DisplayName("测试搜索功能 - 空关键词")
    public void testSearchByKeyword_Empty() {
        // 准备测试数据
        String keyword = "";

        // 配置mock行为
        when(searchMapper.searchByKeyword("%%")).thenReturn(Collections.emptyList());

        // 执行测试
        List<SearchResult> actualResults = searchService.searchByKeyword(keyword);

        // 验证结果
        assertNotNull(actualResults);
        assertTrue(actualResults.isEmpty());
        
        // 验证mock方法被调用
        verify(searchMapper, times(1)).searchByKeyword("%%");
    }

    @Test
    @DisplayName("测试搜索功能 - 特殊字符关键词")
    public void testSearchByKeyword_SpecialCharacters() {
        // 准备测试数据
        String keyword = "!@#$%";

        // 配置mock行为
        when(searchMapper.searchByKeyword("%" + keyword + "%")).thenReturn(Collections.emptyList());

        // 执行测试
        List<SearchResult> actualResults = searchService.searchByKeyword(keyword);

        // 验证结果
        assertNotNull(actualResults);
        assertTrue(actualResults.isEmpty());
        
        // 验证mock方法被调用
        verify(searchMapper, times(1)).searchByKeyword("%" + keyword + "%");
    }

    @Test
    @DisplayName("测试搜索结果字段完整性")
    public void testSearchResult_Fields() {
        // 准备测试数据
        String keyword = "篮球";
        List<SearchResult> expectedResults = Collections.singletonList(mockSearchResult);

        // 配置mock行为
        when(searchMapper.searchByKeyword("%" + keyword + "%")).thenReturn(expectedResults);

        // 执行测试
        List<SearchResult> actualResults = searchService.searchByKeyword(keyword);

        // 验证结果
        assertNotNull(actualResults);
        assertEquals(1, actualResults.size());
        
        SearchResult result = actualResults.get(0);
        assertNotNull(result.getSubsectionId());
        assertNotNull(result.getChapterName());
        assertNotNull(result.getSectionName());
        assertNotNull(result.getSubsectionName());
        assertNotNull(result.getContent());
    }

    @Test
    @DisplayName("测试搜索功能 - 空指针异常")
    public void testSearchByKeyword_Null() {
        // 准备测试数据
        String keyword = null;

        // 配置mock行为
        when(searchMapper.searchByKeyword("%null%")).thenReturn(Collections.emptyList());

        // 执行测试
        List<SearchResult> actualResults = searchService.searchByKeyword(keyword);

        // 验证结果
        assertNotNull(actualResults);
        assertTrue(actualResults.isEmpty());
        
        // 验证mock方法被调用
        verify(searchMapper, times(1)).searchByKeyword("%null%");
    }
} 