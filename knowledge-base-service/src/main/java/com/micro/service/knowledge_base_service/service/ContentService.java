package com.micro.service.knowledge_base_service.service;

import com.micro.service.knowledge_base_service.vo.SubsectionContentVO;

public interface ContentService {
    SubsectionContentVO getSubsectionContent(String chapterId, String sectionId, String subsectionId);
}
