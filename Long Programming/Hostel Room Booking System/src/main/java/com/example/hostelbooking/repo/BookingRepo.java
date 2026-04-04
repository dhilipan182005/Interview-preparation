package com.example.hostelbooking.repo;

import com.example.hostelbooking.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepo extends JpaRepository<Booking, Long> {
    List<Booking> findByUserUserId(Long userId);
}