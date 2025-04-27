package com.software.controller;

import com.software.DTO.LoginResponse;
import com.software.DTO.LoginUserDto;
import com.software.DTO.RegisterRequest; // RegisterUserDto yerine RegisterRequest kullanıyoruz
import com.software.DTO.UserDTO;
import com.software.service.AuthService;
import com.software.service.UserService;
import com.software.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthController(AuthService authService, UserService userService, JwtUtil jwtUtil) {
        this.authService = authService;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@RequestBody RegisterRequest registerRequest) {
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

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginUserDto loginUserDto) {
        try {
            String token = authService.loginUser(loginUserDto.getUsername(), loginUserDto.getPassword());
            LoginResponse loginResponse = new LoginResponse("Bearer " + token, jwtUtil.getEXPIRATION_TIME());
            return ResponseEntity.ok(loginResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }
}