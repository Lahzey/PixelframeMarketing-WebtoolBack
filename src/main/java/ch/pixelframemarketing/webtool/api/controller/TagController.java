package ch.pixelframemarketing.webtool.api.controller;

import ch.pixelframemarketing.webtool.api.dto.TagDTO;
import ch.pixelframemarketing.webtool.api.dto.UserDTO;
import ch.pixelframemarketing.webtool.data.entity.Tag;
import ch.pixelframemarketing.webtool.logic.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
@RequestMapping("/api/tags")
public class TagController {
    
    @Autowired
    private TagService tagService;

    @GetMapping
    public ResponseEntity<TagDTO[]> getAllTags() {
        Tag[] tags = tagService.getAllTags();
        return ResponseEntity.ok(Arrays.stream(tags).map(tag -> new TagDTO(tag)).toArray(TagDTO[]::new));
    }
}
