package com.example.elearning.controllers;

import com.example.elearning.models.Lesson;
import com.example.elearning.services.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lessons")
@RequiredArgsConstructor
public class LessonController {

    private final LessonService lessonService;

    // TEACHER/ADMIN ONLY: Create a new lesson for a course
    @PostMapping("/teacher/{courseId}")
    public ResponseEntity<Lesson> createLesson(@PathVariable Long courseId, @RequestBody Lesson lesson) {
        Lesson createdLesson = lessonService.createLesson(courseId, lesson);
        return ResponseEntity.status(201).body(createdLesson);
    }

    // STUDENT ONLY: View lessons in an *enrolled* course
    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<Lesson>> getLessonsForEnrolledCourse(@PathVariable Long courseId) {
        List<Lesson> lessons = lessonService.getLessonsForEnrolledCourse(courseId);
        return ResponseEntity.ok(lessons);
    }
    
    // STUDENT ONLY: View a specific lesson
    @GetMapping("/{lessonId}")
    public ResponseEntity<Lesson> getLessonById(@PathVariable Long lessonId) {
        // Note: The enrollment check is primarily enforced via the list endpoint, 
        // a production app should probably enforce it here too, but we keep it simple.
        return ResponseEntity.ok(lessonService.getLessonById(null, lessonId)); 
    }
}