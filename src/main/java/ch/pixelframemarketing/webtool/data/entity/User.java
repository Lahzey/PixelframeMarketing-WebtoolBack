package ch.pixelframemarketing.webtool.data.entity;

import ch.pixelframemarketing.webtool.api.dto.UserDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "user_account")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", unique = true, updatable = false)
    private String id;
    @Column(unique=true)
    private String username;
    @Column(unique=true)
    private String email;
    private String password;
    
    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToOne
    @JoinColumn(name = "image_metadata_id", nullable = false)
    private ImageMetadata imageMetadata;
    
    private Date createdAt;
    
    public User(String id) {
        this.id = id;
    }
    
    public User(UserDTO userDTO, ImageMetadata fetchedImageMetadata) {
        username = userDTO.username;
        email = userDTO.email;
        password = userDTO.password;
        role = userDTO.role;
        imageMetadata = fetchedImageMetadata;
        createdAt = new Date();
    }
    
    public enum Role {
        GAME_DEV,
        ADVERTISER,
        ADMIN
    }
}
