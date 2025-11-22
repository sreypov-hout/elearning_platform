package com.example.elearning.services;

import com.example.elearning.models.Role;
import com.example.elearning.models.User;
import com.example.elearning.repositories.UserRepository;
import com.example.elearning.config.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
// CRITICAL IMPORT for breaking the cycle
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration; 
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService; // <--- CRITICAL: Defines the interface
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService { // <--- Implementation resolves compilation

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; 
    private final JwtUtil jwtUtil;
    // CRITICAL FIX: Inject the Configuration instead of the final Manager bean to break the cycle
    private final AuthenticationConfiguration authenticationConfiguration;

    // --- Core Logic: UserDetailsService Implementation (Required by the interface) ---
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }

    // --- Core Logic: Registration and Login ---

    public User registerUser(String username, String password, Role role) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists.");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password)); 
        user.setRole(role);

        return userRepository.save(user);
    }

    public String login(String username, String password) {
        AuthenticationManager authenticationManager;
        try {
            // Retrieve the manager from the configuration object here (on demand)
            authenticationManager = authenticationConfiguration.getAuthenticationManager();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Authentication service failed to initialize.", e);
        }
        
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password.");
        }

        final UserDetails userDetails = loadUserByUsername(username);
        return jwtUtil.generateToken(userDetails);
    }
    
    public User getCurrentUser() {
        String username = (String) org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found in context."));
    }
}