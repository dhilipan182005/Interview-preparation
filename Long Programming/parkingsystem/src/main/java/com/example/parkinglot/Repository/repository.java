package com.example.parkinglot.Repository;

import com.example.parkinglot.DSO.vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface repository extends JpaRepository<vehicle, Integer> {

    List<vehicle> findByVehiclename(String vehiclename);

}