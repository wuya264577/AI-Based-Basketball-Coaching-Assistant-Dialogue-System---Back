package com.micro.service.knowledge_base_service.controller;

import com.micro.service.knowledge_base_service.entity.Article;
import com.micro.service.knowledge_base_service.service.ArticleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

// 文章管理控制器，提供文章的增删查改和统计相关接口
@RestController
@RequestMapping("/api/articles")
public class ArticleController {
    
    private static final Logger logger = LoggerFactory.getLogger(ArticleController.class);

    // 注入文章服务，处理文章相关业务逻辑
    @Autowired
    private ArticleService articleService;

    // 添加新文章，自动设置上传时间和下载量
    @PostMapping
    public Article addArticle(@RequestBody Article article) {
        article.setUploadTime(LocalDateTime.now());
        article.setDownloads(0);
        Article saved = articleService.addArticle(article);
        logger.info("添加新文章: {}", article.getTitle());
        return saved;
    }

    // 删除指定ID的文章
    @DeleteMapping("/{id}")
    public void deleteArticle(@PathVariable Long id) {
        articleService.deleteArticle(id);
        logger.info("删除文章，ID: {}", id);
    }

    // 获取所有文章列表
    @GetMapping
    public List<Article> getAllArticles() {
        logger.debug("获取所有文章列表");
        return articleService.getAllArticles();
    }

    // 根据ID获取单篇文章
    @GetMapping("/{id}")
    public Optional<Article> getArticleById(@PathVariable Long id) {
        logger.debug("根据ID获取文章: {}", id);
        return articleService.getArticleById(id);
    }

    // 增加文章浏览量，返回更新后的文章对象
    @PutMapping("/views/increment/{id}")
    public Article incrementViews(@PathVariable Long id) {
        try {
            Article updated = articleService.incrementViews(id)
                    .orElseThrow(() -> new RuntimeException("Article not found with id: " + id));
            logger.info("增加文章浏览量，ID: {}", id);
            return updated;
        } catch (Exception e) {
            logger.error("增加浏览量失败，ID: {}，异常: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    // 获取浏览量最多的前8篇文章
    @GetMapping("/views/top8")
    public List<Article> getTop8ArticlesByViews() {
        logger.debug("获取浏览量最多的前8篇文章");
        return articleService.getTop8ByViews();
    }

    // 获取上传时间最新的前8篇文章
    @GetMapping("/recent/top8")
    public List<Article> getTop8RecentArticles() {
        logger.debug("获取上传时间最新的前8篇文章");
        return articleService.getTop8ByUploadTime();
    }

    // 获取下载量最多的前8篇文章
    @GetMapping("/downloads/top8")
    public List<Article> getTop8ArticlesByDownloads() {
        logger.debug("获取下载量最多的前8篇文章");
        return articleService.getTop8ByDownloads();
    }

    // 增加文章下载量，返回更新后的文章对象
    @PutMapping("/downloads/increment/{id}")
    public Article incrementDownloads(@PathVariable Long id) {
        try {
            Article updated = articleService.incrementDownloads(id)
                    .orElseThrow(() -> new RuntimeException("Article not found with id: " + id));
            logger.info("增加文章下载量，ID: {}", id);
            return updated;
        } catch (Exception e) {
            logger.error("增加下载量失败，ID: {}，异常: {}", id, e.getMessage(), e);
            throw e;
        }
    }
}
