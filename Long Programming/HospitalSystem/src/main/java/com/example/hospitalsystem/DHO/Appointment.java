package com.example.hospitalsystem.DHO;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name="Appointment")
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ap_id;

    @Column(name="date")
    private LocalDate date;

    @Column(name="reason")
    private String reason;

    @ManyToOne
    @JoinColumn(name = "pa_id")
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "doc_id")
    private Doctor doctor;

    @OneToOne(mappedBy = "appointment", cascade = CascadeType.ALL)
    private Prescription prescription;

    @OneToOne(mappedBy = "appointment", cascade = CascadeType.ALL)
    private Payment payment;
}