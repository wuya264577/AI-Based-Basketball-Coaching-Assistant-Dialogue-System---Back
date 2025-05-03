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

    // 根据知识类型搜索
    @GetMapping("/type/{type}")
    public ResponseEntity<List<BasketballKnowledge>> getByKnowledgeType(
            @PathVariable String type) {
        List<BasketballKnowledge> results = service.searchByKnowledgeType(type);
        return ResponseEntity.ok(results);
    }

    // 根据难度等级搜索
    @GetMapping("/level/{level}")
    public ResponseEntity<List<BasketballKnowledge>> getByDifficultyLevel(
            @PathVariable String level) {
        List<BasketballKnowledge> results = service.searchByDifficultyLevel(level);
        return ResponseEntity.ok(results);
    }

    // 根据知识类型和难度等级搜索
    @GetMapping("/type/{type}/level/{level}")
    public ResponseEntity<List<BasketballKnowledge>> getByTypeAndLevel(
            @PathVariable String type,
            @PathVariable String level) {
        List<BasketballKnowledge> results = service.searchByTypeAndLevel(type, level);
        return ResponseEntity.ok(results);
    }

    // 获取所有视频教程
    @GetMapping("/videos")
    public ResponseEntity<List<BasketballKnowledge>> getAllVideoTutorials() {
        List<BasketballKnowledge> results = service.getAllVideoTutorials();
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

    // 添加新知识
    @PostMapping
    public ResponseEntity<BasketballKnowledge> addKnowledge(@RequestBody BasketballKnowledge knowledge) {
        BasketballKnowledge savedKnowledge = service.addKnowledge(knowledge);
        return ResponseEntity.ok(savedKnowledge);
    }

    // 更新知识
    @PutMapping("/{id}")
    public ResponseEntity<BasketballKnowledge> updateKnowledge(
            @PathVariable Long id,
            @RequestBody BasketballKnowledge knowledge) {
        BasketballKnowledge updatedKnowledge = service.updateKnowledge(id, knowledge);
        if (updatedKnowledge != null) {
            return ResponseEntity.ok(updatedKnowledge);
        }
        return ResponseEntity.notFound().build();
    }

    // 删除知识
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteKnowledge(@PathVariable Long id) {
        if (service.deleteKnowledge(id)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
} 