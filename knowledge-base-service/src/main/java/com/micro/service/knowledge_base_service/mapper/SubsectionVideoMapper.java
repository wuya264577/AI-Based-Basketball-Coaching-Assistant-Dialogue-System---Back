package com.micro.service.knowledge_base_service.mapper;

import com.micro.service.knowledge_base_service.entity.SubsectionVideo;
import lombok.Data;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface SubsectionVideoMapper {
    @Select("SELECT id, subsection_id, video_url, platform, description FROM subsection_video WHERE subsection_id = #{subsectionId}")
    List<SubsectionVideo> findBySubsectionId(@Param("subsectionId") String subsectionId);
}
