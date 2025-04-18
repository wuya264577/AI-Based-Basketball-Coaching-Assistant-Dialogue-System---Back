package com.micro.service.knowledge_base_service.repository;

import com.micro.service.knowledge_base_service.entity.BasketballKnowledge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface BasketballKnowledgeRepository extends JpaRepository<BasketballKnowledge, Long> {
    
    // 根据标题模糊搜索
    List<BasketballKnowledge> findByTitleContainingIgnoreCase(String title);
    
    // 根据分类查询
    List<BasketballKnowledge> findByCategory(String category);
    
    // 复合搜索（标题、内容、关键词）
    @Query("SELECT bk FROM BasketballKnowledge bk WHERE " +
           "LOWER(bk.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(bk.content) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(bk.keywords) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<BasketballKnowledge> searchByKeyword(@Param("searchTerm") String searchTerm);
} 