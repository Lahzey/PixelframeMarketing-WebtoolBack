package ch.pixelframemarketing.webtool.api.controller;

import ch.pixelframemarketing.webtool.api.dto.UserDTO;
import ch.pixelframemarketing.webtool.data.entity.User;
import ch.pixelframemarketing.webtool.logic.service.UserService;
import ch.pixelframemarketing.webtool.general.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    
    @Autowired
    private UserService userService;
    
    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@RequestBody UserDTO userDTO) {
        User user = userService.createUser(userDTO);
        return ResponseEntity.ok(new UserDTO(user));
    }
    
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserDTO loginRequest) {
        if (!userService.login(loginRequest.email, loginRequest.password)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        String jwt = jwtTokenUtil.generateToken(userService.getCurrentUser());
        
        return ResponseEntity.ok(jwt);
    }
}