package com.example.attendancestudentmanagementsystem.repository;

import com.example.attendancestudentmanagementsystem.entity.Attendance;
import com.example.attendancestudentmanagementsystem.entity.Subject;
import com.example.attendancestudentmanagementsystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    boolean existsByStudentAndSubjectAndDate(User student, Subject subject, LocalDate date);

    List<Attendance> findByStudent(User student);

    List<Attendance> findByStudent_Id(Long studentId);

    List<Attendance> findByDate(LocalDate date);

    List<Attendance> findByStudentAndDate(User student, LocalDate date);

    List<Attendance> findBySubjectAndDate(Subject subject, LocalDate date);

    long countByStudent_Id(Long studentId);

    long countByStudent_IdAndStatusIgnoreCase(Long studentId, String status);
}