package com.micro.service.knowledge_base_service.controller;

import com.micro.service.knowledge_base_service.entity.Article;
import com.micro.service.knowledge_base_service.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/articles")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    // 添加文章
    @PostMapping
    public Article addArticle(@RequestBody Article article) {
        article.setUploadTime(LocalDateTime.now());
        article.setDownloads(0);
        return articleService.addArticle(article);
    }

    // 删除文章
    @DeleteMapping("/{id}")
    public void deleteArticle(@PathVariable Long id) {
        articleService.deleteArticle(id);
    }

    // 获取所有文章
    @GetMapping
    public List<Article> getAllArticles() {
        return articleService.getAllArticles();
    }

    // 获取单篇文章
    @GetMapping("/{id}")
    public Optional<Article> getArticleById(@PathVariable Long id) {
        return articleService.getArticleById(id);
    }

    @PutMapping("/views/increment/{id}")
    public Article incrementViews(@PathVariable Long id) {
        return articleService.incrementViews(id)
                .orElseThrow(() -> new RuntimeException("Article not found with id: " + id));
    }

    // 获取浏览量最多的前8篇文章
    @GetMapping("/views/top8")
    public List<Article> getTop8ArticlesByViews() {
        return articleService.getTop8ByViews();
    }

    // 获取上传时间最新的前8篇文章
    @GetMapping("/recent/top8")
    public List<Article> getTop8RecentArticles() {
        return articleService.getTop8ByUploadTime();
    }
}
