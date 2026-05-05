package com.example.hospitalsystem.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.hospitalsystem.DHO.Doctor;
import com.example.hospitalsystem.Repository.DoctorRepo;

import java.util.List;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepo dr;

    public List<Doctor> getAllDoctors() {
        return dr.findAll();
    }

    public Doctor createDoctor(Doctor d) {
        return dr.save(d);
    }

    public Doctor updateDoctor(int id, Doctor d) {
        Doctor existing = dr.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        existing.setDoc_name(d.getDoc_name());
        existing.setSpecialization(d.getSpecialization());
        existing.setConsultation_fee(d.getConsultation_fee());

        return dr.save(existing);
    }

    public String deleteDoctor(int id) {
        if (dr.existsById(id)) {
            dr.deleteById(id);
            return "Doctor deleted";
        }
        throw new RuntimeException("Doctor not found");
    }
}