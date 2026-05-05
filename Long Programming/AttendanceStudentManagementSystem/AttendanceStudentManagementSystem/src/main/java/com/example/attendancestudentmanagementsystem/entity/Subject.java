package com.example.attendancestudentmanagementsystem.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String subjectName;

    @Column(unique = true, nullable = false)
    private String subjectCode;

    public String getName() {
        return subjectName;
    }
}