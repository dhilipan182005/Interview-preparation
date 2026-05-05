package com.example.hospitalsystem.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.hospitalsystem.DHO.Patient;
import com.example.hospitalsystem.Repository.PatientRepo;

import java.util.List;

@Service
public class PatientService {

    @Autowired
    private PatientRepo pr;

    public List<Patient> getAllPatients() {
        return pr.findAll();
    }

    public Patient createPatient(Patient p) {
        return pr.save(p);
    }

    public Patient updatePatient(int id, Patient p) {
        Patient existing = pr.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        existing.setPatient_name(p.getPatient_name());
        existing.setGender(p.getGender());
        existing.setBlood_group(p.getBlood_group());
        existing.setPhone_no(p.getPhone_no());

        return pr.save(existing);
    }

    public String deletePatient(int id) {
        if (pr.existsById(id)) {
            pr.deleteById(id);
            return "Patient deleted";
        }
        throw new RuntimeException("Patient not found");
    }
}