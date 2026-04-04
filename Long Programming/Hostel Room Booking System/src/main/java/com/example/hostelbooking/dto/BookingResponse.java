package com.example.hostelbooking.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookingResponse {

    private Long bookingId;

    private String userName;

    private String roomNumber;
    private String roomType;

    private Date checkIn;
    private Date checkOut;

    private double totalAmount;
}