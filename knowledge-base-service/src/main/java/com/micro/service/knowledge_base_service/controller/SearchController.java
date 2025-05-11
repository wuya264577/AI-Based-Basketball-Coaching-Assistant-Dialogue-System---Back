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

@RestController
@RequestMapping("/api/search")
public class SearchController {
    @Autowired
    private SearchService searchService;

    @PostMapping
    public List<SearchResult> search(@RequestBody SearchRequest request) {
        return searchService.searchByKeyword(request.getKeyword());
    }
}
