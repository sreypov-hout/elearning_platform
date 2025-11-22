package com.example.elearning;

import com.example.elearning.models.Role;
import com.example.elearning.models.User;
import com.example.elearning.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class ElearningDataLoader {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Executes code immediately after the application context is loaded.
     * Creates a default ADMIN user if the database is empty.
     */
    @Bean
    public CommandLineRunner initDatabase() {
        return args -> {
            // Check if any users exist in the database
            if (userRepository.count() == 0) {
                System.out.println("------------------------------------------");
                System.out.println("🚀 Initializing Default Admin User...");
                
                // Create the default Admin user
                User admin = new User();
                admin.setUsername("admin");
                // Password must be encoded
                admin.setPassword(passwordEncoder.encode("adminpass")); 
                admin.setRole(Role.ADMIN);
                
                userRepository.save(admin);
                
                System.out.println("✅ Default Admin created:");
                System.out.println("   Username: admin");
                System.out.println("   Password: adminpass");
                System.out.println("   Role:     ADMIN");
                System.out.println("------------------------------------------");
            }
        };
    }
}