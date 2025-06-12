package com.micro.service.knowledge_base_service.service;

import com.micro.service.knowledge_base_service.entity.Article;
import com.micro.service.knowledge_base_service.repository.ArticleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * 文章服务测试类
 */
@ExtendWith({SpringExtension.class, MockitoExtension.class})
public class ArticleServiceTest {

    @InjectMocks
    private ArticleService articleService;

    @Mock
    private ArticleRepository articleRepository;

    private Article mockArticle;

    @BeforeEach
    public void setUp() {
        // 初始化测试数据
        mockArticle = new Article();
        mockArticle.setId(1L);
        mockArticle.setTitle("测试文章");
        mockArticle.setContent("测试内容");
        mockArticle.setUploadTime(LocalDateTime.now());
        mockArticle.setDownloads(0);
        mockArticle.setViews(0);
    }

    @Test
    @DisplayName("测试添加文章 - 正常情况")
    public void testAddArticle_Normal() {
        // 配置mock行为
        when(articleRepository.save(any(Article.class))).thenReturn(mockArticle);

        // 执行测试
        Article result = articleService.addArticle(mockArticle);

        // 验证结果
        assertNotNull(result);
        assertEquals(mockArticle.getTitle(), result.getTitle());
        assertEquals(mockArticle.getContent(), result.getContent());
        assertEquals(0, result.getDownloads());
        assertEquals(0, result.getViews());

        // 验证mock方法调用
        verify(articleRepository).save(any(Article.class));
    }

    @Test
    @DisplayName("测试删除文章 - 正常情况")
    public void testDeleteArticle_Normal() {
        // 执行测试
        articleService.deleteArticle(1L);

        // 验证mock方法调用
        verify(articleRepository).deleteById(1L);
    }

    @Test
    @DisplayName("测试获取文章 - 正常情况")
    public void testGetArticleById_Normal() {
        // 配置mock行为
        when(articleRepository.findById(1L)).thenReturn(Optional.of(mockArticle));

        // 执行测试
        Optional<Article> result = articleService.getArticleById(1L);

        // 验证结果
        assertTrue(result.isPresent());
        assertEquals(mockArticle.getTitle(), result.get().getTitle());
        assertEquals(mockArticle.getContent(), result.get().getContent());

        // 验证mock方法调用
        verify(articleRepository).findById(1L);
    }

    @Test
    @DisplayName("测试增加浏览量 - 正常情况")
    public void testIncrementViews_Normal() {
        // 配置mock行为
        when(articleRepository.findById(1L)).thenReturn(Optional.of(mockArticle));
        when(articleRepository.save(any(Article.class))).thenReturn(mockArticle);

        // 执行测试
        Optional<Article> result = articleService.incrementViews(1L);

        // 验证结果
        assertTrue(result.isPresent());
        assertEquals(1, result.get().getViews());

        // 验证mock方法调用
        verify(articleRepository).findById(1L);
        verify(articleRepository).save(any(Article.class));
    }

    @Test
    @DisplayName("测试增加下载量 - 正常情况")
    public void testIncrementDownloads_Normal() {
        // 配置mock行为
        when(articleRepository.findById(1L)).thenReturn(Optional.of(mockArticle));
        when(articleRepository.save(any(Article.class))).thenReturn(mockArticle);

        // 执行测试
        Optional<Article> result = articleService.incrementDownloads(1L);

        // 验证结果
        assertTrue(result.isPresent());
        assertEquals(1, result.get().getDownloads());

        // 验证mock方法调用
        verify(articleRepository).findById(1L);
        verify(articleRepository).save(any(Article.class));
    }

    @Test
    @DisplayName("测试获取热门文章 - 正常情况")
    public void testGetTop8ByViews_Normal() {
        // 准备测试数据
        List<Article> mockArticles = Arrays.asList(mockArticle);

        // 配置mock行为
        when(articleRepository.findTop8ByOrderByViewsDesc()).thenReturn(mockArticles);

        // 执行测试
        List<Article> results = articleService.getTop8ByViews();

        // 验证结果
        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals(mockArticle.getTitle(), results.get(0).getTitle());

        // 验证mock方法调用
        verify(articleRepository).findTop8ByOrderByViewsDesc();
    }

    @Test
    @DisplayName("测试获取最新文章 - 正常情况")
    public void testGetTop8ByUploadTime_Normal() {
        // 准备测试数据
        List<Article> mockArticles = Arrays.asList(mockArticle);

        // 配置mock行为
        when(articleRepository.findTop8ByOrderByUploadTimeDesc()).thenReturn(mockArticles);

        // 执行测试
        List<Article> results = articleService.getTop8ByUploadTime();

        // 验证结果
        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals(mockArticle.getTitle(), results.get(0).getTitle());

        // 验证mock方法调用
        verify(articleRepository).findTop8ByOrderByUploadTimeDesc();
    }

    @Test
    @DisplayName("测试获取文章 - 文章不存在")
    public void testGetArticleById_NotFound() {
        // 配置mock行为
        when(articleRepository.findById(999L)).thenReturn(Optional.empty());

        // 执行测试
        Optional<Article> result = articleService.getArticleById(999L);

        // 验证结果
        assertFalse(result.isPresent());

        // 验证mock方法调用
        verify(articleRepository).findById(999L);
    }

    @Test
    @DisplayName("测试添加文章 - 无效数据")
    public void testAddArticle_InvalidData() {
        // 准备测试数据
        Article invalidArticle = new Article();
        // 不设置必要字段

        // 配置mock行为
        when(articleRepository.save(any(Article.class))).thenReturn(invalidArticle);

        // 执行测试
        Article result = articleService.addArticle(invalidArticle);

        // 验证结果
        assertNotNull(result);
        assertNull(result.getTitle());
        assertNull(result.getContent());

        // 验证mock方法调用
        verify(articleRepository).save(any(Article.class));
    }
}