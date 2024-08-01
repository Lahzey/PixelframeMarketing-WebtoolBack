package ch.pixelframemarketing.webtool.api.dto;

import ch.pixelframemarketing.webtool.data.entity.Tag;

public class TagDTO {
    public String name;
    public String categoryName;
    public String categoryColor;
    
    public TagDTO(Tag tag) {
        this.name = tag.getName();
        this.categoryName = tag.getCategory().name;
        this.categoryColor = tag.getCategory().color;
    }
}
