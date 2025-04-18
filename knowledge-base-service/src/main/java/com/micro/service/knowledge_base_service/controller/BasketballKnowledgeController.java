package com.micro.service.knowledge_base_service.controller;

import com.micro.service.knowledge_base_service.entity.BasketballKnowledge;
import com.micro.service.knowledge_base_service.service.BasketballKnowledgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/knowledge")
@CrossOrigin(origins = "*", maxAge = 3600)
public class BasketballKnowledgeController {

    @Autowired
    private BasketballKnowledgeService service;

    // 搜索知识
    @GetMapping("/search")
    public ResponseEntity<List<BasketballKnowledge>> searchKnowledge(
            @RequestParam(required = false) String query) {
        List<BasketballKnowledge> results = service.searchKnowledge(query);
        return ResponseEntity.ok(results);
    }

    // 按分类搜索
    @GetMapping("/category/{category}")
    public ResponseEntity<List<BasketballKnowledge>> getByCategory(
            @PathVariable String category) {
        List<BasketballKnowledge> results = service.searchByCategory(category);
        return ResponseEntity.ok(results);
    }

    // 获取所有知识
    @GetMapping
    public ResponseEntity<List<BasketballKnowledge>> getAllKnowledge() {
        List<BasketballKnowledge> knowledge = service.getAllKnowledge();
        return ResponseEntity.ok(knowledge);
    }

    // 获取具体知识
    @GetMapping("/{id}")
    public ResponseEntity<BasketballKnowledge> getKnowledgeById(@PathVariable Long id) {
        BasketballKnowledge knowledge = service.getKnowledgeById(id);
        if (knowledge != null) {
            return ResponseEntity.ok(knowledge);
        }
        return ResponseEntity.notFound().build();
    }
} 