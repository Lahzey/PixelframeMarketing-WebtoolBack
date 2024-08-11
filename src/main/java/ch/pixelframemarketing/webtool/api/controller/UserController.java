package ch.pixelframemarketing.webtool.api.controller;

import ch.pixelframemarketing.webtool.api.dto.UserDTO;
import ch.pixelframemarketing.webtool.data.entity.User;
import ch.pixelframemarketing.webtool.api.interceptor.Secure;
import ch.pixelframemarketing.webtool.logic.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    @Secure
    public ResponseEntity<UserDTO> getUser(@PathVariable("id") String id) {
        User user = userService.findUserById(id).orElse(null);
        if (user == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(new UserDTO(user));
    }

    @PutMapping("/{id}")
    @Secure
    public ResponseEntity<UserDTO> updateUser(@PathVariable("id") String id, @RequestBody UserDTO userDTO) {
        userDTO.id = id;
        User user = userService.updateUser(userDTO);
        return ResponseEntity.ok(new UserDTO(user));
    }
}