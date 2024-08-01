package ch.pixelframemarketing.webtool.data.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity(name = "page")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Page {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", unique = true, updatable = false)
    private String id;
    
    @Column
    @Lob
    private String content;

    private Date createdAt;
    
    public Page(String content) {
        this.content = content;
        this.createdAt = new Date();
    }
    
}
