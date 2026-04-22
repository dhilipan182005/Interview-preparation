package com.example.hospitalsystem.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.hospitalsystem.DHO.Payment;

public interface PaymentRepo extends JpaRepository<Payment, Integer> {
}