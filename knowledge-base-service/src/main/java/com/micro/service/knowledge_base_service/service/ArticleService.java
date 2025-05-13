package com.micro.service.knowledge_base_service.service;

import com.micro.service.knowledge_base_service.entity.Article;
import com.micro.service.knowledge_base_service.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ArticleService {
    @Autowired
    private ArticleRepository articleRepository;

    // 添加
    public Article addArticle(Article article) {
        return articleRepository.save(article);
    }

    // 删除
    public void deleteArticle(Long id) {
        articleRepository.deleteById(id);
    }

    // 查找全部
    public List<Article> getAllArticles() {
        return articleRepository.findAll();
    }

    // 根据 ID 查找
    public Optional<Article> getArticleById(Long id) {
        return articleRepository.findById(id);
    }

    // 增加浏览量
    public Optional<Article> incrementViews(Long id) {
        Optional<Article> optionalArticle = articleRepository.findById(id);
        if (optionalArticle.isPresent()) {
            Article article = optionalArticle.get();
            article.setViews(article.getViews() + 1);
            articleRepository.save(article);
        }
        return optionalArticle;
    }

    // 获取浏览量前8的文章
    public List<Article> getTop8ByViews() {
        return articleRepository.findTop8ByOrderByViewsDesc();
    }

    // 获取最新上传的前8篇文章
    public List<Article> getTop8ByUploadTime() {
        return articleRepository.findTop8ByOrderByUploadTimeDesc();
    }

}
