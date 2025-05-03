package com.micro.service.knowledge_base_service.service;

import com.micro.service.knowledge_base_service.entity.AIConsultation;
import com.micro.service.knowledge_base_service.entity.BasketballKnowledge;
import java.util.List;

public interface AIService {
    
    // 获取AI回答
    String getAIResponse(String question, String userId);
    
    // 获取相关篮球知识
    List<BasketballKnowledge> getRelatedKnowledge(String question);
    
    // 保存咨询记录
    AIConsultation saveConsultation(AIConsultation consultation);
    
    // 更新用户反馈
    AIConsultation updateFeedback(Long consultationId, Boolean isSatisfied, String feedback);
    
    // 获取用户历史咨询记录
    List<AIConsultation> getUserConsultationHistory(String userId);
    
    // 获取热门问题
    List<String> getPopularQuestions();
    
    // 获取推荐问题
    List<String> getRecommendedQuestions(String userId);
} 