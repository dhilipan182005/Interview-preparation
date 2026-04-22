package com.example.hospitalsystem.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.hospitalsystem.DHO.Prescription;

public interface PrescriptionRepo extends JpaRepository<Prescription, Integer> {
}