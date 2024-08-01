package ch.pixelframemarketing.webtool.data.entity;

import ch.pixelframemarketing.webtool.general.enums.TagCategory;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "tag")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Tag {
    
    @Id
    private String name;
    
    @Enumerated(EnumType.STRING)
    private TagCategory category;
    
}
