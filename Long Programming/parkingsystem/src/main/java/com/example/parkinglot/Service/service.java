package com.example.parkinglot.Service;

import com.example.parkinglot.DSO.vehicle;
import com.example.parkinglot.Repository.repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class service {
    @Autowired
    private repository reposi;
    public List<vehicle> getvehiclename() {
        return reposi.findAll();
    }

    public vehicle saved(vehicle v) {
        return reposi.save(v);
    }

    public  vehicle updatevehicle(int vehicleid, vehicle v) {
        vehicle existing = reposi.findById(vehicleid).orElseThrow(() -> new RuntimeException("Vehicle not found"));

            existing.setVehiclename(v.getVehiclename());
            existing.setOwnername(v.getOwnername());
            return reposi.save(existing);
    }

    public String delete(int vehicleid) {
        if(reposi.existsById(vehicleid)) {
            reposi.deleteById(vehicleid);
            return "Deleted successfully";
        }
        else {
            throw new RuntimeException("Vehicle not fount with id"+vehicleid);
        }
    }
}
