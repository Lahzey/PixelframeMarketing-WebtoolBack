package ch.pixelframemarketing.webtool.api.dto;

import ch.pixelframemarketing.webtool.data.entity.ImageMetadata;
import ch.pixelframemarketing.webtool.data.entity.User;
import ch.pixelframemarketing.webtool.data.repository.ImageRepository;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.ALWAYS)
public class UserDTO {
    
    public String id;
    public String username;
    public String email;
    public String password;
    public User.Role role;
    public String imageId;
    
    public UserDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        // not including password, even sending a hashed password to the frontend is a security risk
        this.role = user.getRole();
        this.imageId = user.getImageMetadata().getId();
    }
    
    public void transferToEntity(User user) {
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.setRole(role);
        user.setImageMetadata(new ImageMetadata(imageId));
    }
    
}
