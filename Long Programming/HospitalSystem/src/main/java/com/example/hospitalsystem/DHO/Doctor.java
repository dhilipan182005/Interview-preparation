package com.example.hospitalsystem.DHO;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name="Doctor")
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int doc_id;

    @Column(name="doc_name")
    private String doc_name;

    @Column(name="specialization")
    private String specialization;

    @Column(name="consultation_fee")
    private double consultation_fee;

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL)
    private List<Appointment> appointments;
}