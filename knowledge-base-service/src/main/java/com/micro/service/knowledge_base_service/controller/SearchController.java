package com.micro.service.knowledge_base_service.controller;

import com.micro.service.knowledge_base_service.dto.SearchRequest;
import com.micro.service.knowledge_base_service.service.SearchService;
import com.micro.service.knowledge_base_service.vo.SearchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// 知识库搜索控制器，提供知识库内容的搜索接口
@RestController
@RequestMapping("/api/search")
public class SearchController {
    
    // 注入搜索服务，处理知识库内容的搜索请求
    @Autowired
    private SearchService searchService;

    // 根据关键词搜索知识库内容
    @PostMapping
    public List<SearchResult> search(@RequestBody SearchRequest request) {
        return searchService.searchByKeyword(request.getKeyword());
    }
}
