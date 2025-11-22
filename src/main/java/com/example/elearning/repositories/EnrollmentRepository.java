package com.example.elearning.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.elearning.models.Course;
import com.example.elearning.models.Enrollment;
import com.example.elearning.models.User;

import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    
    // Check if a student is already enrolled in a course
    Optional<Enrollment> findByStudentAndCourse(User student, Course course);
    
    // Get all enrollments for a specific student
    List<Enrollment> findByStudent(User student);
}
