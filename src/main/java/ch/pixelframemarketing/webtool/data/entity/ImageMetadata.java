package ch.pixelframemarketing.webtool.data.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "image_metadata")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class ImageMetadata {
    @Id
    @Column(name = "id", unique = true, updatable = false)
    private String id;
    
    private String fileType;
    
    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = true)
    private User owner;
    
    private String dropboxShareLink;
    
    public ImageMetadata(String id){
        this.id = id;
    }
    
    public String getFileName() {
        return id + "." + fileType;
    }
}