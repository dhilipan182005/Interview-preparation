package com.example.hospitalsystem.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.hospitalsystem.DHO.Patient;
import com.example.hospitalsystem.Service.PatientService;

import java.util.List;

@RestController
@RequestMapping("/Patient")
@CrossOrigin(origins = "http://localhost:63342")
public class PatientController {

    @Autowired
    private PatientService ps;

    @GetMapping("/get")
    public List<Patient> getPatients() {
        return ps.getAllPatients();
    }

    @PostMapping("/create")
    public Patient createPatient(@RequestBody Patient p) {
        return ps.createPatient(p);
    }

    @PutMapping("/{id}")
    public Patient updatePatient(@RequestBody Patient p, @PathVariable int id) {
        return ps.updatePatient(id, p);
    }

    @DeleteMapping("/{id}")
    public String deletePatient(@PathVariable int id) {
        return ps.deletePatient(id);
    }
}