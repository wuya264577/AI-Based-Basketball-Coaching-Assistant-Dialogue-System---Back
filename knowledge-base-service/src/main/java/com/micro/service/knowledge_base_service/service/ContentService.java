package com.micro.service.knowledge_base_service.service;

import com.micro.service.knowledge_base_service.vo.ContentWithSubsectionVO;
import com.micro.service.knowledge_base_service.vo.SubsectionContentVO;

import java.util.List;

public interface ContentService {
    SubsectionContentVO getSubsectionContent(String chapterId, String sectionId, String subsectionId);

    List<ContentWithSubsectionVO> getContentBySectionName(String sectionName);
}
