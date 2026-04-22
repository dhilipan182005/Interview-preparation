package com.example.hospitalsystem.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.hospitalsystem.DHO.Doctor;

public interface DoctorRepo extends JpaRepository<Doctor, Integer> {
}