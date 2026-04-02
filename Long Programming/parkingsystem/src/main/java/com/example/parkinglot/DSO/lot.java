package com.example.parkinglot.DSO;

import jakarta.persistence.*;

@Entity
public class lot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int lotid;
    @Column(name = "lotname")
    private String lotname;

}
