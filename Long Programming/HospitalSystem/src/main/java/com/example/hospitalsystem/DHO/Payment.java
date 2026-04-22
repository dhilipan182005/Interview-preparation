package com.example.hospitalsystem.DHO;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name="Payment")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int payment_id;

    @Column(name="billing_date")
    private LocalDate billing_date;

    @Column(name="total_amount")
    private double total_amount;

    @Column(name="payment_status")
    private String payment_status;

    @OneToOne
    @JoinColumn(name = "app_id")
    private Appointment appointment;
}