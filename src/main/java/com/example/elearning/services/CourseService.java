package com.example.elearning.services;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.elearning.models.Course;
import com.example.elearning.models.User;
import com.example.elearning.repositories.CourseRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final AuthService authService; // Used to get current Teacher

    // Teacher/Admin function
    public Course createCourse(Course course) {
        // Automatically assign the currently logged-in teacher
        User teacher = authService.getCurrentUser();
        course.setTeacher(teacher);
        return courseRepository.save(course);
    }

    // Student/Public function
    public List<Course> findAllCourses() {
        return courseRepository.findAll();
    }
    
    // Helper function
    public Course findCourseById(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found."));
    }
    
    // Teacher function to ensure the course belongs to the teacher
    public Course findCourseByIdAndCheckTeacher(Long courseId, Long teacherId) {
        Course course = findCourseById(courseId);
        if (!course.getTeacher().getId().equals(teacherId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied: Course does not belong to this teacher.");
        }
        return course;
    }
}
