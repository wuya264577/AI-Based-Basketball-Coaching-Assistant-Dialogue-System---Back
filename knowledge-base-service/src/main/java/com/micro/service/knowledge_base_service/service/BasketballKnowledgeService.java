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

    // 根据分类搜索
    public List<BasketballKnowledge> searchByCategory(String category) {
        return repository.findByCategory(category);
    }

    // 根据标题搜索
    public List<BasketballKnowledge> searchByTitle(String title) {
        return repository.findByTitleContainingIgnoreCase(title);
    }

    // 获取所有知识
    public List<BasketballKnowledge> getAllKnowledge() {
        return repository.findAll();
    }

    // 根据ID获取具体知识
    public BasketballKnowledge getKnowledgeById(Long id) {
        return repository.findById(id).orElse(null);
    }
} 