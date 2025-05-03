package com.micro.service.knowledge_base_service.controller;

import com.micro.service.knowledge_base_service.entity.AIConsultation;
import com.micro.service.knowledge_base_service.entity.BasketballKnowledge;
import com.micro.service.knowledge_base_service.service.AIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ai")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AIController {

    @Autowired
    private AIService aiService;

    // 获取AI回答
    @PostMapping("/ask")
    public ResponseEntity<String> askQuestion(
            @RequestBody Map<String, String> request) {
        String question = request.get("question");
        String userId = request.get("userId");
        
        String response = aiService.getAIResponse(question, userId);
        return ResponseEntity.ok(response);
    }

    // 获取相关问题知识
    @PostMapping("/related-knowledge")
    public ResponseEntity<List<BasketballKnowledge>> getRelatedKnowledge(
            @RequestBody Map<String, String> request) {
        String question = request.get("question");
        List<BasketballKnowledge> knowledge = aiService.getRelatedKnowledge(question);
        return ResponseEntity.ok(knowledge);
    }

    // 提交用户反馈
    @PostMapping("/feedback/{consultationId}")
    public ResponseEntity<AIConsultation> submitFeedback(
            @PathVariable Long consultationId,
            @RequestBody Map<String, Object> request) {
        Boolean isSatisfied = (Boolean) request.get("isSatisfied");
        String feedback = (String) request.get("feedback");
        
        AIConsultation consultation = aiService.updateFeedback(consultationId, isSatisfied, feedback);
        return ResponseEntity.ok(consultation);
    }

    // 获取用户历史咨询记录
    @GetMapping("/history/{userId}")
    public ResponseEntity<List<AIConsultation>> getConsultationHistory(
            @PathVariable String userId) {
        List<AIConsultation> history = aiService.getUserConsultationHistory(userId);
        return ResponseEntity.ok(history);
    }

    // 获取热门问题
    @GetMapping("/popular-questions")
    public ResponseEntity<List<String>> getPopularQuestions() {
        List<String> questions = aiService.getPopularQuestions();
        return ResponseEntity.ok(questions);
    }

    // 获取推荐问题
    @GetMapping("/recommended-questions/{userId}")
    public ResponseEntity<List<String>> getRecommendedQuestions(
            @PathVariable String userId) {
        List<String> questions = aiService.getRecommendedQuestions(userId);
        return ResponseEntity.ok(questions);
    }
} 