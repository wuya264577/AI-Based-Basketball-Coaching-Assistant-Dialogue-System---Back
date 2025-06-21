package com.micro.service.knowledge_base_service.service;

import com.micro.service.knowledge_base_service.mapper.SearchMapper;
import com.micro.service.knowledge_base_service.vo.SearchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

// 搜索服务实现，负责根据关键词进行知识内容的模糊查询
@Service
public class SearchServiceImpl implements SearchService {
    // 注入搜索数据访问对象
    @Autowired
    private SearchMapper searchMapper;

    // 根据关键词进行模糊搜索，返回匹配的内容列表
    @Override
    public List<SearchResult> searchByKeyword(String keyword) {
        // 添加通配符进行模糊搜索
        return searchMapper.searchByKeyword("%" + keyword + "%");
    }
}
