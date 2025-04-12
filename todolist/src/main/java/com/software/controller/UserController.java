package com.software.controller;

import com.software.DTO.*;
import com.software.service.AuthService; // Yeni eklenen AuthService
import com.software.service.UserService;
import com.software.util.JwtUtil;
import io.jsonwebtoken.JwtException;
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

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@RequestBody LoginUserDto loginUserDto) {
        try {
            String token = authService.loginUser(loginUserDto.getEmail(), loginUserDto.getPassword());
            LoginResponse loginResponse = new LoginResponse("Bearer " + token, jwtUtil.getEXPIRATION_TIME());
            return ResponseEntity.ok(loginResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerUser(@RequestBody RegisterRequest registerRequest) {
        try {
            UserDTO createdUser = userService.registerUser(
                    registerRequest.getName(),
                    registerRequest.getSurname(),
                    registerRequest.getEmail(),
                    registerRequest.getUsername(),
                    registerRequest.getPassword()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @DeleteMapping("/delete/{username}")
    public ResponseEntity<String> deleteUser(@PathVariable String username) {
        try {
            String result = userService.deleteUser(username);
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }

    @PutMapping("/update-email/{username}")
    public ResponseEntity<UserDTO> updateEmail(@PathVariable String username, @RequestBody String newEmail) {
        try {
            UserDTO updatedUser = userService.updateEmail(username, newEmail);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PutMapping("/update-password/{username}")
    public ResponseEntity<String> updatePassword(@PathVariable String username,
                                                 @RequestParam String currentPassword,
                                                 @RequestParam String newPassword) {
        try {
            String result = userService.updatePassword(username, currentPassword, newPassword);
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/update-username/{oldUsername}")
    public ResponseEntity<String> updateUsername(@PathVariable String oldUsername, @RequestBody String newUsername) {
        try {
            String newToken = userService.updateUsername(oldUsername, newUsername);
            return ResponseEntity.ok(newToken);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PutMapping("/update-name/{username}")
    public ResponseEntity<UserDTO> updateNameAndSurname(@PathVariable String username,
                                                        @RequestBody UserDTO updatedInfo) {

        try {
            UserDTO updatedUser = userService.updateNameAndSurname(
                    username,
                    updatedInfo.getName(),
                    updatedInfo.getSurname()
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
}