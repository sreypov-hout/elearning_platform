package com.example.elearning.controllers;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.elearning.models.Role;
import com.example.elearning.models.User;
import com.example.elearning.services.AuthService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // A simple DTO/Record replacement for registration requests
    @Data
    private static class AuthRequest {
        private String username;
        private String password;
    }

    // A simple DTO/Record replacement for successful login response
    @Data
    private static class AuthResponse {
        private String token;
        private String role;
        private Long userId;
    }
    
    // PUBLIC endpoint for students to register
    @PostMapping("/register/student")
    public ResponseEntity<User> registerStudent(@RequestBody AuthRequest request) {
        User user = authService.registerUser(request.getUsername(), request.getPassword(), Role.STUDENT);
        // Clean up password before returning
        user.setPassword(null); 
        return ResponseEntity.ok(user);
    }
    
    // ADMIN-ONLY endpoint to register a teacher
    @PostMapping("/register/teacher")
    public ResponseEntity<User> registerTeacher(@RequestBody AuthRequest request) {
        User user = authService.registerUser(request.getUsername(), request.getPassword(), Role.TEACHER);
        user.setPassword(null); 
        return ResponseEntity.ok(user);
    }
    
    // ADMIN-ONLY endpoint to register another admin (optional setup/seed)
    @PostMapping("/register/admin")
    public ResponseEntity<User> registerAdmin(@RequestBody AuthRequest request) {
        User user = authService.registerUser(request.getUsername(), request.getPassword(), Role.ADMIN);
        user.setPassword(null); 
        return ResponseEntity.ok(user);
    }
    
    // PUBLIC endpoint for login
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        String token = authService.login(request.getUsername(), request.getPassword());
        
        // Retrieve the user to include their role and ID in the response
        User user = (User) authService.loadUserByUsername(request.getUsername());

        AuthResponse response = new AuthResponse();
        response.setToken(token);
        response.setRole(user.getRole().name());
        response.setUserId(user.getId());
        
        return ResponseEntity.ok(response);
    }
}