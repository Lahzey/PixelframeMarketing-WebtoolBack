package ch.pixelframemarketing.webtool.data.entity;

import ch.pixelframemarketing.webtool.general.enums.Visibility;
import ch.pixelframemarketing.webtool.general.exception.ValidationException;
import ch.pixelframemarketing.webtool.general.util.StringArrayConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductListing {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", unique = true, updatable = false)
    private String id;
    
    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;
    
    private Date createdAt;

    private String title;
    
    private Visibility visibility;
    
    @Convert(converter = StringArrayConverter.class)
    private String[] tags;
    
    private String ageRestriction;

    @ManyToOne
    @JoinColumn(name = "thumbnail_metadata_id", nullable = false)
    private ImageMetadata thumbnailMetadata;

    @Column(length = 500)
    private String description;
    
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "page_id", nullable = false)
    private Page page;
    
    
    public void validate(ProductListing existing) throws ValidationException {
        boolean exists = existing != null;

        Map<String, String> errors = new HashMap<String, String>();
        
        if (title == null || title.trim().isEmpty()) {
            errors.put("title", "Title cannot be empty");
        }
        if (description == null || description.trim().isEmpty()) {
            errors.put("description", "Description cannot be empty");
        }
        if (page == null || page.getContent() == null || page.getContent().trim().isEmpty()) {
            errors.put("pageContent", "Page Content cannot be empty");
        }
        
        if (!errors.isEmpty()) throw new ValidationException(errors);

        // properties that the user may not change
        setCreatedAt(exists ? existing.getCreatedAt() : new Date());
    }

}
