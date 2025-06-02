package com.micro.service.knowledge_base_service.controller;

import com.micro.service.knowledge_base_service.entity.Article;
import com.micro.service.knowledge_base_service.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 文章管理控制器
 * 提供文章的增删改查以及统计功能
 */
@RestController
@RequestMapping("/api/articles")
public class ArticleController {
    
    /**
     * 文章服务接口
     * 用于处理文章的增删改查等业务逻辑
     */
    @Autowired
    private ArticleService articleService;

    /**
     * 添加新文章
     * 
     * article 文章信息
     *  添加成功后的文章对象
     */
    @PostMapping
    public Article addArticle(@RequestBody Article article) {
        article.setUploadTime(LocalDateTime.now());
        article.setDownloads(0);
        return articleService.addArticle(article);
    }

    /**
     * 删除指定文章
     * 
     * id 文章ID
     */
    @DeleteMapping("/{id}")
    public void deleteArticle(@PathVariable Long id) {
        articleService.deleteArticle(id);
    }

    /**
     * 获取所有文章列表
     * 
     *  文章列表
     */
    @GetMapping
    public List<Article> getAllArticles() {
        return articleService.getAllArticles();
    }

    /**
     * 根据ID获取单篇文章
     * 
     * am id 文章ID
     * return 文章信息（如果存在）
     */
    @GetMapping("/{id}")
    public Optional<Article> getArticleById(@PathVariable Long id) {
        return articleService.getArticleById(id);
    }

    /**
     * 增加文章浏览量
     * 
     * param id 文章ID
     * return 更新后的文章对象
     * throws RuntimeException 当文章不存在时抛出异常
     */
    @PutMapping("/views/increment/{id}")
    public Article incrementViews(@PathVariable Long id) {
        return articleService.incrementViews(id)
                .orElseThrow(() -> new RuntimeException("Article not found with id: " + id));
    }

    /**
     * 获取浏览量最多的前8篇文章
     * 
     * return 文章列表
     */
    @GetMapping("/views/top8")
    public List<Article> getTop8ArticlesByViews() {
        return articleService.getTop8ByViews();
    }

    /**
     * 获取上传时间最新的前8篇文章
     * 
     * return 文章列表
     */
    @GetMapping("/recent/top8")
    public List<Article> getTop8RecentArticles() {
        return articleService.getTop8ByUploadTime();
    }

    /**
     * 获取下载量最多的前8篇文章
     * 
     *return 文章列表
     */
    @GetMapping("/downloads/top8")
    public List<Article> getTop8ArticlesByDownloads() {
        return articleService.getTop8ByDownloads();
    }

    /**
     * 增加文章下载量
     * 
     * param id 文章ID
     * return 更新后的文章对象
     * throws RuntimeException 当文章不存在时抛出异常
     */
    @PutMapping("/downloads/increment/{id}")
    public Article incrementDownloads(@PathVariable Long id) {
        return articleService.incrementDownloads(id)
                .orElseThrow(() -> new RuntimeException("Article not found with id: " + id));
    }
}
