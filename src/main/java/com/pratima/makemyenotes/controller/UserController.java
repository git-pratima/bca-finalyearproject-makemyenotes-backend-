package com.pratima.makemyenotes.controller;

import com.pratima.makemyenotes.dto.Response;
import com.pratima.makemyenotes.dto.UserDTO;
import com.pratima.makemyenotes.entity.User;
import com.pratima.makemyenotes.repository.UserRepository;
import com.pratima.makemyenotes.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> getAllUsers(){
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Response> updateUser(@PathVariable Long id,  @RequestBody UserDTO userDTO){
        return ResponseEntity.ok(userService.updateUser(id, userDTO));
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> deleteUser(@PathVariable Long id){
        return ResponseEntity.ok(userService.deleteUser(id));
    }

    @GetMapping("/current")
    public ResponseEntity<User> getCurrentUser(){
        return ResponseEntity.ok(userService.getCurrentLoggedInUser());
    }

    @GetMapping("/online")
    public Response getOnlineUsers() {
        LocalDateTime threshold = LocalDateTime.now().minusMinutes(60);
        List<User> onlineUsers = userRepository.findOnlineUsers(threshold);

        List<UserDTO> onlineUserDTOs = onlineUsers.stream()
                .map(user -> UserDTO.builder()
                        .id(user.getId())
                        .name(user.getName())
                        .email(user.getEmail())
                        .role(user.getRole())
                        .lastActiveAt(user.getLastactiveat())
                        .isOnline(user.isOnline())
                        .provider(String.valueOf(user.getProvider()))
                        .joined(user.getCreatedAt())
                        .build())
                .collect(Collectors.toList());

        return Response.builder()
                .status(200)
                .message(onlineUsers.size() + " user(s) online")
                .users(onlineUserDTOs)
                .build();
    }
}
