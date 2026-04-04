package com.example.hostelbooking.controller;

import com.example.hostelbooking.dto.BookingResponse;
import com.example.hostelbooking.entity.Booking;
import com.example.hostelbooking.services.BookingServices;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/booking")
public class BookingController {

    private final BookingServices bookingServices;

    public BookingController(BookingServices bookingServices) {
        this.bookingServices = bookingServices;
    }

    @PostMapping("/{userId}/{roomId}")
    public Booking createBooking(@PathVariable Long userId,
                                 @PathVariable Long roomId,
                                 @RequestBody Booking booking) {
        return bookingServices.createBooking(userId, roomId, booking);
    }

    @GetMapping("/GETBOOKING")
    public List<BookingResponse> getAllBookings() {
        return bookingServices.getAllBookings();
    }
}