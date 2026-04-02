package com.example.parkinglot.DSO;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int vehicleid;
    @Column(name = "vehiclename")
    private String vehiclename;
    @Column(name = "ownername")
    private String ownername;
}
