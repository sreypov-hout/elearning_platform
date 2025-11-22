package com.example.elearning.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.elearning.models.Course;

public interface CourseRepository extends JpaRepository<Course, Long> {
}
