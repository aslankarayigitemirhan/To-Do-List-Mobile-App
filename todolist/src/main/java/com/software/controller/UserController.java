package com.software.controller;

import com.software.DTO.*;
import com.software.model.User;
import com.software.service.AuthService; // Yeni eklenen AuthService
import com.software.service.UserService;
import com.software.util.JwtUtil;
import io.jsonwebtoken.JwtException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final AuthService authService; // AuthService eklendi
    private final JwtUtil jwtUtil;

    @Autowired
    public UserController(UserService userService, AuthService authService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.authService = authService;
        this.jwtUtil = jwtUtil;
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteUser(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Authorization header missing or invalid");
            }

            String result = userService.deleteUser(authorizationHeader);
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }

    @PutMapping("/update-email")
    public ResponseEntity<UserDTO> updateEmail(@RequestHeader("Authorization") String authorizationHeader,@RequestParam String newEmail) {
        try {
            UserDTO updatedUser = userService.updateEmail(authorizationHeader,newEmail);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PutMapping("/update-password")
    public ResponseEntity<String> updatePassword(@RequestHeader("Authorization") String authorizationHeader,
                                                 @RequestParam String currentPassword,
                                                 @RequestParam String newPassword) {
        try {
            String result = userService.updatePassword(authorizationHeader, currentPassword, newPassword);
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/update-username")
    public ResponseEntity<String> updateUsername(@RequestHeader("Authorization") String authorizationHeader, @RequestParam String newUsername) {
        try {
            String newToken = userService.updateUsername(authorizationHeader, newUsername);
            return ResponseEntity.ok(newToken);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PutMapping("/update-name")
    public ResponseEntity<UserDTO> updateNameAndSurname(@RequestHeader("Authorization") String authorizationHeader,@RequestParam String name, @RequestParam String surname) {

        try {
            UserDTO updatedUser = userService.updateNameAndSurname(
                    authorizationHeader,name,surname
            );
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
    @GetMapping("/getTags")
    public ResponseEntity<?> getUserTags(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            // Extract token from "Bearer <token>"
            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Authorization header missing or invalid");
            }
            String token = authorizationHeader.replace("Bearer ", ""); // Remove "Bearer " prefix

            // Call UserService to get tags
            List<TagDTO> tags = userService.getUserTags(token);
            return ResponseEntity.ok(tags);

        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User not found: " + e.getMessage());
        } catch (JwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid or expired token: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred: " + e.getMessage());
        }
    }
    @Transactional
    @GetMapping("/getProfile")
    public ResponseEntity<UserDTO> getProfile(@RequestHeader("Authorization") String authorizationHeader){
        String token = authorizationHeader.replace("Bearer ", ""); // Remove "Bearer " prefix
        String username = jwtUtil.extractUsername(token);
        User user = this.userService.getUserByUsername(username);
        return ResponseEntity.ok(new UserDTO(user.getName(),user.getSurname(),user.getEmail(),user.getUsername(),user.getOwnerId()));
    }
}