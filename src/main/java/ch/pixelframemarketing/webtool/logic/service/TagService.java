package ch.pixelframemarketing.webtool.logic.service;

import ch.pixelframemarketing.webtool.data.entity.Tag;
import ch.pixelframemarketing.webtool.data.repository.TagRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@Slf4j
@Transactional(rollbackOn = Exception.class)
@RequiredArgsConstructor
public class TagService {
    
    @Autowired
    private TagRepository tagRepository;
    
    public Tag[] getAllTags() {
        List<Tag> tags = new ArrayList<>(tagRepository.findAll()); // creating new list makes sure it is modifiable
        tags.sort(Comparator.comparing((Tag tag) -> tag.getCategory().name));
        return tags.toArray(Tag[]::new);
    }
    
}
