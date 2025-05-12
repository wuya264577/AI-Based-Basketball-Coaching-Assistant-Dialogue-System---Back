package com.micro.service.knowledge_base_service.repository;

import com.micro.service.knowledge_base_service.entity.AIConsultation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AIConsultationRepository extends JpaRepository<AIConsultation, Long> {
    
    // 根据用户ID查询咨询记录，按时间倒序排序
    List<AIConsultation> findByUserIdOrderByCreatedAtDesc(String userId);
    
    // 根据知识类型查询咨询记录
    List<AIConsultation> findByKnowledgeType(String knowledgeType);
    
    // 查询用户满意度为true的记录
    List<AIConsultation> findByUserIdAndIsSatisfiedTrue(String userId);
} 