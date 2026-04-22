package com.example.hospitalsystem.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.hospitalsystem.DHO.Appointment;
import com.example.hospitalsystem.Service.AppointmentService;
import java.util.List;

@RestController
@RequestMapping("/Appointment")
public class AppointmentController {
    @Autowired
    private AppointmentService as;

    @GetMapping("/get")
    public List<Appointment> getAppointments() {
        return as.getAllAppointments();
    }

    @PostMapping("/create")
    public Appointment postAppointment(@RequestBody Appointment a) {
        return as.postAppointment(a);
    }

    @PutMapping("/{id}")
    public Appointment putAppointment(@RequestBody Appointment a, @PathVariable int id) {
        return as.putAppointment(id, a);
    }

    @DeleteMapping("/{id}")
    public String deleteAppointment(@PathVariable int id) {
        return as.deleteAppointment(id);
    }
}