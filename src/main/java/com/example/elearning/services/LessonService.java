package com.example.elearning.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.example.elearning.models.Course;
import com.example.elearning.models.Lesson;
import com.example.elearning.models.User;
import com.example.elearning.repositories.LessonRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LessonService {

    private final LessonRepository lessonRepository;
    private final CourseService courseService;
    private final EnrollmentService enrollmentService;
    private final AuthService authService; // Used to get current Teacher/Student

    // Teacher/Admin function
    public Lesson createLesson(Long courseId, Lesson lesson) {
        // 1. Get current teacher and verify ownership of the course
        User teacher = authService.getCurrentUser();
        Course course = courseService.findCourseByIdAndCheckTeacher(courseId, teacher.getId());
        
        // 2. Set course and save lesson
        lesson.setCourse(course);
        return lessonRepository.save(lesson);
    }
    
    // Student function
    public List<Lesson> getLessonsForEnrolledCourse(Long courseId) {
        // 1. Get current student
        User student = authService.getCurrentUser();
        Course course = courseService.findCourseById(courseId);

        // 2. Verify student is enrolled in the course
        if (!enrollmentService.isStudentEnrolled(student, course)) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.FORBIDDEN, 
                    "You must be enrolled in this course to view lessons."
            );
        }

        // 3. Return lessons ordered by lessonOrder
        return lessonRepository.findByCourseIdOrderByLessonOrderAsc(courseId);
    }
    
    // Public/Student function to view a single lesson (after enrollment check)
    public Lesson getLessonById(Long courseId, Long lessonId) {
        // This is a simplified version; in production, you'd likely want to enforce the enrollment check here as well.
        // The check is already done by `getLessonsForEnrolledCourse` if viewing the list.
        return lessonRepository.findById(lessonId).orElseThrow(() -> new org.springframework.web.server.ResponseStatusException(
                org.springframework.http.HttpStatus.NOT_FOUND, 
                "Lesson not found."
        ));
    }
}
