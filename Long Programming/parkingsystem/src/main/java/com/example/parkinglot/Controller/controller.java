package com.example.parkinglot.Controller;

import com.example.parkinglot.DSO.vehicle;
import com.example.parkinglot.Service.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class controller {
    @Autowired
    private service Service;
    @GetMapping("/vehicle")
    public List<vehicle> getVehiclename(){ return Service.getvehiclename();
    }
    @PostMapping("/update")
    public vehicle addvehicle(@RequestBody vehicle v){

        return Service.saved(v);
    }
    @PutMapping("/{id}")
    public vehicle updateVehicle(@PathVariable("id") int vehicleid, @RequestBody vehicle v) {
        return Service.updatevehicle(vehicleid, v);
    }

    @DeleteMapping("/{id}")
    public String deletevehicle(@PathVariable("id") int vehicleid){
        return Service.delete(vehicleid);
    }

}
