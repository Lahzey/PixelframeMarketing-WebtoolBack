package ch.pixelframemarketing.webtool.logic.service;

import ch.pixelframemarketing.webtool.data.entity.User;
import ch.pixelframemarketing.webtool.api.dto.UserDTO;
import ch.pixelframemarketing.webtool.data.repository.ImageRepository;
import ch.pixelframemarketing.webtool.data.repository.UserRepository;
import ch.pixelframemarketing.webtool.general.exception.ValidationException;
import lombok.Getter;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.util.HashMap;
import java.util.Map;

@Service
@SessionScope
public class UserService {
    private static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();
    
    private static final int MIN_USERNAME_LENGTH = 4;
    private static final int MAX_USERNAME_LENGTH = 30;
    private static final int MIN_PASSWORD_LENGTH = 8;
    private static final int MAX_PASSWORD_LENGTH = 100;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ImageRepository imageRepository;
    
    @Getter
    private User currentUser = null;

    public User findUserById(String id) {
        return userRepository.findById(id).orElseThrow();
    }
    
    public boolean login(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user == null || !PASSWORD_ENCODER.matches(password, user.getPassword())) return false;
        currentUser = user;
        return true;
    }

    /**
     * Logs the user in directly using a userId. This should only ever be used if the requesters identity has already been confirmed,
     * for example with a signed token.
     * @param userId the id of the user to log into
     * @return true if that user was found, false otherwise
     */
    public boolean loginById(String userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) return false;
        currentUser = user;
        return true;
    }

    public User createUser(UserDTO userDTO) {
        validateUserDTO(userDTO);
        
        userDTO.password = PASSWORD_ENCODER.encode(userDTO.password);
        User user = new User();
        userDTO.transferToEntity(user, imageRepository);
        return userRepository.save(user);
    }

    public User updateUser(UserDTO userDTO) {
        validateUserDTO(userDTO);
        
        User user = findUserById(userDTO.id);
        userDTO.transferToEntity(user, imageRepository);
        return userRepository.save(user);
    }
    
    public void validateCurrentUser(String ownerId) {
        if (currentUser == null) throw new SecurityException();
        if (currentUser.getRole() == User.Role.ADMIN) return; // Admins are allowed to use all APIs on all resources
        
        if (!currentUser.getId().equals(ownerId)) throw new SecurityException();
    }

    private void validateUserDTO(UserDTO userDTO) {
        Map<String, String> errors = new HashMap<>();
        if (userDTO.imageId == null || userDTO.imageId.isEmpty()) userDTO.imageId = ImageService.DEFAULT_USER_IMAGE_ID;
        
        if (userDTO.username == null || userDTO.username.isEmpty()) errors.put("username", "Username cannot be empty");
        else if (userDTO.username.length() < MIN_USERNAME_LENGTH) errors.put("username", "Username must be at least " + MIN_USERNAME_LENGTH + " characters");
        else if (userDTO.username.length() > MAX_USERNAME_LENGTH) errors.put("username", "Username cannot exceed " + MAX_USERNAME_LENGTH + " characters");
        
        if (userDTO.password == null || userDTO.password.isEmpty()) errors.put("password", "Password cannot be empty");
        else if (userDTO.password.length() < MIN_PASSWORD_LENGTH) errors.put("password", "Password must be at least " + MIN_PASSWORD_LENGTH + " characters");
        else if (userDTO.password.length() > MAX_PASSWORD_LENGTH) errors.put("password", "Password cannot exceed " + MAX_PASSWORD_LENGTH + " characters");
        
        if (userDTO.email == null || userDTO.email.isEmpty()) errors.put("email", "Email cannot be empty");
        else if (!EmailValidator.getInstance().isValid(userDTO.email)) errors.put("email", "Invalid email");
        
        // ADMIN accounts must be setup manually on the DB
        if (userDTO.role != User.Role.ADVERTISER && userDTO.role != User.Role.GAME_DEV) errors.put("role", "Invalid Role");
        
        if (!errors.isEmpty()) throw new ValidationException(errors);
    }
}