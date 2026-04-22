package com.example.hospitalsystem.DHO;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name="Prescription")
public class Prescription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int pre_id;

    @Column(name="date")
    private LocalDate date;

    @Column(name="medicine")
    private String medicine;

    @OneToOne
    @JoinColumn(name = "ap_id")
    private Appointment appointment;
}