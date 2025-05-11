package com.micro.service.knowledge_base_service.service;

import com.micro.service.knowledge_base_service.vo.SearchResult;

import java.util.List;

public interface SearchService {
    List<SearchResult> searchByKeyword(String keyword);
}
