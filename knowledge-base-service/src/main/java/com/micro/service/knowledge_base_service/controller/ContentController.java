package com.micro.service.knowledge_base_service.controller;

import com.micro.service.knowledge_base_service.service.ContentService;
import com.micro.service.knowledge_base_service.vo.ContentWithSubsectionVO;
import com.micro.service.knowledge_base_service.vo.SubsectionContentVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 知识库内容控制器
 * 提供知识库内容的查询和管理功能
 */
@RestController
@RequestMapping("/api/content")
public class ContentController {

    /**
     * 内容服务接口
     * 用于处理知识库内容的查询和管理
     */
    @Autowired
    private ContentService contentService;

    /**
     * 获取指定章节、小节和子节的内容
     * 
     * param chapterId 章节ID
     * param sectionId 小节ID
     * param subsectionId 子节ID
     * return 包含子节内容的响应实体
     */
    @GetMapping
    public ResponseEntity<SubsectionContentVO> getSubsectionContent(
            @RequestParam String chapterId,
            @RequestParam String sectionId,
            @RequestParam String subsectionId) {

        SubsectionContentVO result = contentService.getSubsectionContent(chapterId, sectionId, subsectionId);
        return ResponseEntity.ok(result);
    }

    /**
     * 根据小节名称获取相关内容
     * 
     * param sectionName 小节名称
     * return 包含小节内容的响应实体列表
     */
    @GetMapping("/by-section-name")
    public ResponseEntity<List<ContentWithSubsectionVO>> getContentBySectionName(
            @RequestParam String sectionName) {

        List<ContentWithSubsectionVO> result = contentService.getContentBySectionName(sectionName);
        return ResponseEntity.ok(result);
    }
}
