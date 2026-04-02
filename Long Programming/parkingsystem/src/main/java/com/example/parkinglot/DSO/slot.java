package com.example.parkinglot.DSO;

import jakarta.persistence.*;

@Entity
public class slot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int slotid;
    @Column(name = "status")
    private String status;
    @ManyToOne
    @JoinColumn(name = "vehicleid")
    private vehicle vehicleid;
}
