package com.micro.service.knowledge_base_service.service;

import com.micro.service.knowledge_base_service.entity.Article;
import com.micro.service.knowledge_base_service.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

// 文章服务，负责文章的增删查改和统计等业务逻辑
@Service
public class ArticleService {
    // 注入文章数据访问对象
    @Autowired
    private ArticleRepository articleRepository;

    // 添加文章
    public Article addArticle(Article article) {
        return articleRepository.save(article);
    }

    // 删除指定ID的文章
    public void deleteArticle(Long id) {
        articleRepository.deleteById(id);
    }

    // 获取所有文章
    public List<Article> getAllArticles() {
        return articleRepository.findAll();
    }

    // 根据ID获取文章
    public Optional<Article> getArticleById(Long id) {
        return articleRepository.findById(id);
    }

    // 增加文章浏览量
    public Optional<Article> incrementViews(Long id) {
        Optional<Article> optionalArticle = articleRepository.findById(id);
        if (optionalArticle.isPresent()) {
            Article article = optionalArticle.get();
            article.setViews(article.getViews() + 1);
            articleRepository.save(article);
        }
        return optionalArticle;
    }

    // 增加文章下载量
    public Optional<Article> incrementDownloads(Long id) {
        Optional<Article> optionalArticle = articleRepository.findById(id);
        if (optionalArticle.isPresent()) {
            Article article = optionalArticle.get();
            article.setDownloads(article.getDownloads() + 1);
            articleRepository.save(article);
        }
        return optionalArticle;
    }

    // 获取浏览量最多的前8篇文章
    public List<Article> getTop8ByViews() {
        return articleRepository.findTop8ByOrderByViewsDesc();
    }

    // 获取最新上传的前8篇文章
    public List<Article> getTop8ByUploadTime() {
        return articleRepository.findTop8ByOrderByUploadTimeDesc();
    }

    // 获取下载量最多的前8篇文章
    public List<Article> getTop8ByDownloads() {
        return articleRepository.findTop8ByOrderByDownloadsDesc();
    }

}
