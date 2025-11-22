package com.example.elearning.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.elearning.models.Course;
import com.example.elearning.services.CourseService;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    // TEACHER/ADMIN ONLY: Create a new course
    @PostMapping("/teacher")
    public ResponseEntity<Course> createCourse(@RequestBody Course course) {
        Course createdCourse = courseService.createCourse(course);
        return ResponseEntity.status(201).body(createdCourse);
    }

    // PUBLIC/AUTHENTICATED: List all courses
    @GetMapping
    public ResponseEntity<List<Course>> getAllCourses() {
        // Note: For a real app, you might want to project this to remove teacher's full user details
        return ResponseEntity.ok(courseService.findAllCourses());
    }

    // PUBLIC/AUTHENTICATED: Get a specific course
    @GetMapping("/{courseId}")
    public ResponseEntity<Course> getCourseById(@PathVariable Long courseId) {
        return ResponseEntity.ok(courseService.findCourseById(courseId));
    }
}
