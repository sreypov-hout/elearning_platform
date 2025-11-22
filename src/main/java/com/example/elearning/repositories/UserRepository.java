package com.example.elearning.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.elearning.models.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
