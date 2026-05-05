package com.example.attendancestudentmanagementsystem.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceResponse {

    private Long id;
    private String subjectName;
    private String status;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
}