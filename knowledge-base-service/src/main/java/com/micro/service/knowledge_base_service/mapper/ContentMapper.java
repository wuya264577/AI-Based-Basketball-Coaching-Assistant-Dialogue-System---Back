package com.micro.service.knowledge_base_service.mapper;

import com.micro.service.knowledge_base_service.vo.ContentWithSubsectionVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface ContentMapper {
    @Select("SELECT ss.subsection_name, c.content " +
            "FROM content c " +
            "JOIN subsection ss ON c.subsection_id = ss.subsection_id " +
            "WHERE c.chapter_id = #{chapterId} AND c.section_id = #{sectionId} AND c.subsection_id = #{subsectionId}")
    Map<String, Object> findContentWithSubsectionName(@Param("chapterId") String chapterId,
                                                      @Param("sectionId") String sectionId,
                                                      @Param("subsectionId") String subsectionId);


    @Select("SELECT c.chapter_id, s.section_id, ss.subsection_id, c.content, ss.subsection_name " +
            "FROM content c " +
            "JOIN section s ON c.section_id = s.section_id " +
            "JOIN subsection ss ON c.subsection_id = ss.subsection_id " +
            "WHERE s.section_name = #{sectionName}")
    List<ContentWithSubsectionVO> findContentBySectionName(@Param("sectionName") String sectionName);
}
