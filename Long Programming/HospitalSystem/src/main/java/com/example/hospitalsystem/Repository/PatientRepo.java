package com.example.hospitalsystem.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.hospitalsystem.DHO.Patient;

public interface PatientRepo extends JpaRepository<Patient, Integer> {
}