package com.example.hospitalsystem.DHO;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name="Patient")
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int patient_id;

    @Column(name="patient_name")
    private String patient_name;

    @Column(name="gender")
    private String gender;

    @Column(name="blood_group")
    private String blood_group;

    @Column(name="phone_no")
    private String phone_no;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    private List<Appointment> appointments;
}