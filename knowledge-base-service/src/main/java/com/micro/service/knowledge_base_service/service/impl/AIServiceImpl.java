package com.micro.service.knowledge_base_service.service.impl;

import com.micro.service.knowledge_base_service.entity.AIConsultation;
import com.micro.service.knowledge_base_service.entity.BasketballKnowledge;
import com.micro.service.knowledge_base_service.repository.AIConsultationRepository;
import com.micro.service.knowledge_base_service.repository.BasketballKnowledgeRepository;
import com.micro.service.knowledge_base_service.service.AIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.*;

@Service
public class AIServiceImpl implements AIService {

    @Autowired
    private AIConsultationRepository consultationRepository;

    @Autowired
    private BasketballKnowledgeRepository knowledgeRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${ai.api.url}")
    private String aiApiUrl;

    @Value("${ai.api.key}")
    private String aiApiKey;

    @Override
    public String getAIResponse(String question, String userId) {
        // 调用AI API获取回答
        Map<String, String> request = new HashMap<>();
        request.put("question", question);
        request.put("userId", userId);
        
        // 这里需要根据实际的AI API进行调整
        String response = restTemplate.postForObject(
            aiApiUrl,
            request,
            String.class
        );

        // 保存咨询记录
        AIConsultation consultation = new AIConsultation();
        consultation.setUserId(userId);
        consultation.setQuestion(question);
        consultation.setAnswer(response);
        consultation.setKnowledgeType(analyzeQuestionType(question));
        saveConsultation(consultation);

        return response;
    }

    @Override
    public List<BasketballKnowledge> getRelatedKnowledge(String question) {
        // 分析问题类型
        String knowledgeType = analyzeQuestionType(question);
        
        // 从知识库中获取相关知识点
        return knowledgeRepository.findByKnowledgeType(knowledgeType);
    }

    @Override
    public AIConsultation saveConsultation(AIConsultation consultation) {
        return consultationRepository.save(consultation);
    }

    @Override
    public AIConsultation updateFeedback(Long consultationId, Boolean isSatisfied, String feedback) {
        AIConsultation consultation = consultationRepository.findById(consultationId)
            .orElseThrow(() -> new RuntimeException("Consultation not found"));
        
        consultation.setIsSatisfied(isSatisfied);
        consultation.setFeedback(feedback);
        
        return consultationRepository.save(consultation);
    }

    @Override
    public List<AIConsultation> getUserConsultationHistory(String userId) {
        return consultationRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    @Override
    public List<String> getPopularQuestions() {
        // 这里可以实现获取热门问题的逻辑
        // 例如：统计最近一段时间内被问得最多的问题
        return Arrays.asList(
            "如何提高投篮命中率？",
            "篮球基本规则是什么？",
            "如何练习运球？"
        );
    }

    @Override
    public List<String> getRecommendedQuestions(String userId) {
        // 这里可以实现个性化推荐问题的逻辑
        // 例如：根据用户历史咨询记录推荐相关问题
        return Arrays.asList(
            "篮球防守技巧有哪些？",
            "如何提高弹跳力？",
            "篮球战术有哪些？"
        );
    }

    private String analyzeQuestionType(String question) {
        // 简单的关键词匹配来确定问题类型
        if (question.contains("投篮") || question.contains("运球") || question.contains("传球")) {
            return "技术动作";
        } else if (question.contains("战术") || question.contains("配合")) {
            return "战术策略";
        } else if (question.contains("体能") || question.contains("训练")) {
            return "体能训练";
        } else {
            return "基础理论";
        }
    }
} 