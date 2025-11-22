package com.example.elearning.services;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.elearning.models.Course;
import com.example.elearning.models.Enrollment;
import com.example.elearning.models.User;
import com.example.elearning.repositories.EnrollmentRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final CourseService courseService;
    private final AuthService authService;

    // Student function
    public Enrollment enrollStudentInCourse(Long courseId) {
        User student = authService.getCurrentUser();
        Course course = courseService.findCourseById(courseId);

        // Check for existing enrollment
        if (enrollmentRepository.findByStudentAndCourse(student, course).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Already enrolled in this course.");
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);

        return enrollmentRepository.save(enrollment);
    }

    // Student function
    public List<Enrollment> getStudentEnrollments() {
        User student = authService.getCurrentUser();
        return enrollmentRepository.findByStudent(student);
    }
    
    // Helper function for LessonService
    public boolean isStudentEnrolled(User student, Course course) {
        return enrollmentRepository.findByStudentAndCourse(student, course).isPresent();
    }
}