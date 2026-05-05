package com.example.hospitalsystem.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.hospitalsystem.DHO.Doctor;
import com.example.hospitalsystem.Service.DoctorService;

import java.util.List;

@RestController
@RequestMapping("/Doctor")
@CrossOrigin(origins = "http://localhost:63342")
public class DoctorController {

    @Autowired
    private DoctorService ds;

    @GetMapping("/get")
    public List<Doctor> getDoctors() {
        return ds.getAllDoctors();
    }

    @PostMapping("/create")
    public Doctor createDoctor(@RequestBody Doctor d) {
        return ds.createDoctor(d);
    }

    @PutMapping("/{id}")
    public Doctor updateDoctor(@RequestBody Doctor d, @PathVariable int id) {
        return ds.updateDoctor(id, d);
    }

    @DeleteMapping("/{id}")
    public String deleteDoctor(@PathVariable int id) {
        return ds.deleteDoctor(id);
    }
}