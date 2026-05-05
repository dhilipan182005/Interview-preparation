package com.example.attendancestudentmanagementsystem.service;

import com.example.attendancestudentmanagementsystem.dto.AttendanceRequest;
import com.example.attendancestudentmanagementsystem.dto.AttendanceResponse;
import com.example.attendancestudentmanagementsystem.entity.Attendance;
import com.example.attendancestudentmanagementsystem.entity.Subject;
import com.example.attendancestudentmanagementsystem.entity.User;
import com.example.attendancestudentmanagementsystem.repository.AttendanceRepository;
import com.example.attendancestudentmanagementsystem.repository.SubjectRepository;
import com.example.attendancestudentmanagementsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final UserRepository userRepository;
    private final SubjectRepository subjectRepository;

    public void markAttendance(AttendanceRequest req) {

        User student = userRepository.findById(req.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found"));

        Subject subject = subjectRepository.findById(req.getSubjectId())
                .orElseThrow(() -> new RuntimeException("Subject not found"));

        boolean alreadyMarked = attendanceRepository
                .existsByStudentAndSubjectAndDate(student, subject, LocalDate.now());

        if (alreadyMarked) {
            throw new RuntimeException("Attendance already marked for today");
        }

        Attendance attendance = new Attendance();
        attendance.setStudent(student);
        attendance.setSubject(subject);
        attendance.setStatus(req.getStatus());
        attendance.setDate(LocalDate.now());

        attendanceRepository.save(attendance);
    }

    public List<AttendanceResponse> getMyAttendance(String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return attendanceRepository.findByStudent(user)
                .stream()
                .map(a -> new AttendanceResponse(
                        a.getId(),
                        a.getSubject().getName(),
                        a.getStatus(),
                        a.getDate()
                ))
                .toList();
    }

    public double getAttendancePercentage(Long studentId) {

        List<Attendance> list = attendanceRepository.findByStudent_Id(studentId);

        long total = list.size();
        long present = list.stream()
                .filter(a -> a.getStatus().equalsIgnoreCase("Present"))
                .count();

        return total == 0 ? 0 : (present * 100.0) / total;
    }
}