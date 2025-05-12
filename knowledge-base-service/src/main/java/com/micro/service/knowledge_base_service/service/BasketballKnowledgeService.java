package com.micro.service.knowledge_base_service.service;

import com.micro.service.knowledge_base_service.entity.BasketballKnowledge;
import com.micro.service.knowledge_base_service.repository.BasketballKnowledgeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BasketballKnowledgeService {

    @Autowired
    private BasketballKnowledgeRepository repository;

    // 搜索知识
    public List<BasketballKnowledge> searchKnowledge(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return repository.findAll();
        }
        return repository.searchByKeyword(searchTerm.trim());
    }

    // 根据知识类型搜索
    public List<BasketballKnowledge> searchByKnowledgeType(String knowledgeType) {
        return repository.findByKnowledgeType(knowledgeType);
    }

    // 根据难度等级搜索
    public List<BasketballKnowledge> searchByDifficultyLevel(String difficultyLevel) {
        return repository.findByDifficultyLevel(difficultyLevel);
    }

    // 根据知识类型和难度等级搜索
    public List<BasketballKnowledge> searchByTypeAndLevel(String knowledgeType, String difficultyLevel) {
        return repository.findByKnowledgeTypeAndDifficultyLevel(knowledgeType, difficultyLevel);
    }

    // 获取所有视频教程
    public List<BasketballKnowledge> getAllVideoTutorials() {
        return repository.findByVideoUrlIsNotNull();
    }

    // 获取所有知识
    public List<BasketballKnowledge> getAllKnowledge() {
        return repository.findAll();
    }

    // 根据ID获取具体知识
    public BasketballKnowledge getKnowledgeById(Long id) {
        return repository.findById(id).orElse(null);
    }

    // 添加新知识
    public BasketballKnowledge addKnowledge(BasketballKnowledge knowledge) {
        return repository.save(knowledge);
    }

    // 更新知识
    public BasketballKnowledge updateKnowledge(Long id, BasketballKnowledge knowledge) {
        if (repository.existsById(id)) {
            knowledge.setId(id);
            return repository.save(knowledge);
        }
        return null;
    }

    // 删除知识
    public boolean deleteKnowledge(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
} 