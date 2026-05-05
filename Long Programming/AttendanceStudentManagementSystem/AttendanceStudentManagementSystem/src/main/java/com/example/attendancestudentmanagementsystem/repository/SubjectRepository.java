package com.example.attendancestudentmanagementsystem.repository;

import com.example.attendancestudentmanagementsystem.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubjectRepository extends JpaRepository<Subject, Long> {

    Optional<Subject> findByName(String name);

    Optional<Subject> findBySubjectCode(String subjectCode);

    boolean existsBySubjectCode(String subjectCode);
}