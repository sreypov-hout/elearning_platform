package com.example.elearning.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.elearning.models.Lesson;

import java.util.List;

public interface LessonRepository extends JpaRepository<Lesson, Long> {
    List<Lesson> findByCourseIdOrderByLessonOrderAsc(Long courseId);
}
