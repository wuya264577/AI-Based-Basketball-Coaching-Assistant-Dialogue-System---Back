package com.micro.service.knowledge_base_service.mapper;

import com.micro.service.knowledge_base_service.vo.ContentWithSubsectionVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ContentMapper {
    @Select("SELECT content FROM content WHERE chapter_id = #{chapterId} AND section_id = #{sectionId} AND subsection_id = #{subsectionId}")
    String findContent(@Param("chapterId") String chapterId,
                       @Param("sectionId") String sectionId,
                       @Param("subsectionId") String subsectionId);

    @Select("SELECT c.chapter_id, s.section_id, ss.subsection_id, c.content, ss.subsection_name " +
            "FROM content c " +
            "JOIN section s ON c.section_id = s.section_id " +
            "JOIN subsection ss ON c.subsection_id = ss.subsection_id " +
            "WHERE s.section_name = #{sectionName}")
    List<ContentWithSubsectionVO> findContentBySectionName(@Param("sectionName") String sectionName);
}
