package com.example.hospitalsystem.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.hospitalsystem.DHO.Appointment;
import com.example.hospitalsystem.DHO.Doctor;
import com.example.hospitalsystem.DHO.Patient;
import com.example.hospitalsystem.Repository.AppointmentRepo;
import com.example.hospitalsystem.Repository.DoctorRepo;
import com.example.hospitalsystem.Repository.PatientRepo;
import java.util.List;

@Service
public class AppointmentService {
    @Autowired
    private AppointmentRepo ar;
    @Autowired
    private PatientRepo pr;
    @Autowired
    private DoctorRepo dr;

    public List<Appointment> getAllAppointments() {
        return ar.findAll();
    }

    public Appointment postAppointment(Appointment a) {
        int patientId = a.getPatient().getPatient_id();
        Patient patient = pr.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        int docId = a.getDoctor().getDoc_id();
        Doctor doctor = dr.findById(docId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        a.setPatient(patient);
        a.setDoctor(doctor);
        return ar.save(a);
    }

    public Appointment putAppointment(int id, Appointment a) {
        Appointment existing = ar.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        existing.setDate(a.getDate());
        existing.setReason(a.getReason());

        int patientId = a.getPatient().getPatient_id();
        Patient patient = pr.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        existing.setPatient(patient);

        int docId = a.getDoctor().getDoc_id();
        Doctor doctor = dr.findById(docId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
        existing.setDoctor(doctor);

        return ar.save(existing);
    }

    public String deleteAppointment(int id) {
        if (ar.existsById(id)) {
            ar.deleteById(id);
            return "Appointment deleted successfully";
        } else {
            throw new RuntimeException("Appointment not found");
        }
    }
}