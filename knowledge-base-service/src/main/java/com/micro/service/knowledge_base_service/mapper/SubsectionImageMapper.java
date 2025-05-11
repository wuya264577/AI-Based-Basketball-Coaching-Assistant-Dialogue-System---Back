package com.micro.service.knowledge_base_service.mapper;

import com.micro.service.knowledge_base_service.entity.SubsectionImage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SubsectionImageMapper {
    @Select("SELECT id, subsection_id, image_url, description FROM subsection_image WHERE subsection_id = #{subsectionId}")
    List<SubsectionImage> findBySubsectionId(@Param("subsectionId") String subsectionId);
}
