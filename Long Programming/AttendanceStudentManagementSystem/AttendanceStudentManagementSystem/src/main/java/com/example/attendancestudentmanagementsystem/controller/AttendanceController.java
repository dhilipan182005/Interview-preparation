package com.example.attendancestudentmanagementsystem.controller;

import com.example.attendancestudentmanagementsystem.dto.AttendanceRequest;
import com.example.attendancestudentmanagementsystem.dto.AttendanceResponse;
import com.example.attendancestudentmanagementsystem.service.AttendanceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;

    @PostMapping("/mark")
    @PreAuthorize("hasRole('FACULTY')")
    public ResponseEntity<String> markAttendance(@Valid @RequestBody AttendanceRequest req) {
        attendanceService.markAttendance(req);
        return ResponseEntity.ok("Attendance marked successfully");
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<AttendanceResponse>> getMyAttendance(Authentication auth) {

        if (auth == null) {
            throw new RuntimeException("Unauthorized");
        }

        return ResponseEntity.ok(
                attendanceService.getMyAttendance(auth.getName())
        );
    }

    @GetMapping("/percentage/{studentId}")
    @PreAuthorize("hasAnyRole('FACULTY','ADMIN')")
    public ResponseEntity<Double> getPercentage(@PathVariable Long studentId) {
        return ResponseEntity.ok(
                attendanceService.getAttendancePercentage(studentId)
        );
    }
}