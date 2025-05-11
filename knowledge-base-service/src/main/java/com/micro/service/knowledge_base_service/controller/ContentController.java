package com.micro.service.knowledge_base_service.controller;

import com.micro.service.knowledge_base_service.service.ContentService;
import com.micro.service.knowledge_base_service.vo.SubsectionContentVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/content")
public class ContentController {

    @Autowired
    private ContentService contentService;

    @GetMapping
    public ResponseEntity<SubsectionContentVO> getSubsectionContent(
            @RequestParam String chapterId,
            @RequestParam String sectionId,
            @RequestParam String subsectionId) {

        SubsectionContentVO result = contentService.getSubsectionContent(chapterId, sectionId, subsectionId);
        return ResponseEntity.ok(result);
    }
}
