package com.micro.service.knowledge_base_service.mapper;

import com.micro.service.knowledge_base_service.vo.SearchResult;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SearchMapper {
    public List<SearchResult> searchByKeyword(@Param("keyword") String keyword);
}
