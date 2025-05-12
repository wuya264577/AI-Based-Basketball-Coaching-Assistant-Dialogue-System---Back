package com.micro.service.knowledge_base_service.repository;

import com.micro.service.knowledge_base_service.entity.BasketballKnowledge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface BasketballKnowledgeRepository extends JpaRepository<BasketballKnowledge, Long> {
    
    // 根据知识类型查询
    List<BasketballKnowledge> findByKnowledgeType(String knowledgeType);
    
    // 根据难度等级查询
    List<BasketballKnowledge> findByDifficultyLevel(String difficultyLevel);
    
    // 复合搜索（标题、内容、关键词）
    @Query("SELECT bk FROM BasketballKnowledge bk WHERE " +
           "LOWER(bk.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(bk.content) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(bk.keywords) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<BasketballKnowledge> searchByKeyword(@Param("searchTerm") String searchTerm);
    
    // 根据知识类型和难度等级查询
    List<BasketballKnowledge> findByKnowledgeTypeAndDifficultyLevel(String knowledgeType, String difficultyLevel);
    
    // 获取有视频的知识
    List<BasketballKnowledge> findByVideoUrlIsNotNull();
} 