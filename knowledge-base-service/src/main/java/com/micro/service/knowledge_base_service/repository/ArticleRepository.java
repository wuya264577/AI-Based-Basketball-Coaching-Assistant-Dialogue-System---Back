package com.micro.service.knowledge_base_service.repository;

import com.micro.service.knowledge_base_service.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleRepository  extends JpaRepository<Article, Long> {
    List<Article> findTop8ByOrderByViewsDesc();
    List<Article> findTop8ByOrderByUploadTimeDesc();
}
