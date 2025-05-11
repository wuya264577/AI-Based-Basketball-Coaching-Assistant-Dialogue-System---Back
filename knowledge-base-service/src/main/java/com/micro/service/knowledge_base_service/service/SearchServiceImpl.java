package com.micro.service.knowledge_base_service.service;

import com.micro.service.knowledge_base_service.mapper.SearchMapper;
import com.micro.service.knowledge_base_service.vo.SearchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchServiceImpl implements SearchService {
    @Autowired
    private SearchMapper searchMapper;

    @Override
    public List<SearchResult> searchByKeyword(String keyword) {
        // 添加通配符进行模糊搜索
        return searchMapper.searchByKeyword("%" + keyword + "%");
    }
}
