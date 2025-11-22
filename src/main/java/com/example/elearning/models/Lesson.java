package com.example.elearning.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    
    @Lob // For longer content
    private String content; 

    private Integer lessonOrder; // To keep lessons in a specific sequence

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;
}