package com.micro.service.knowledge_base_service.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ContentMapper {
    @Select("SELECT content FROM content WHERE chapter_id = #{chapterId} AND section_id = #{sectionId} AND subsection_id = #{subsectionId}")
    String findContent(@Param("chapterId") String chapterId,
                       @Param("sectionId") String sectionId,
                       @Param("subsectionId") String subsectionId);
}
