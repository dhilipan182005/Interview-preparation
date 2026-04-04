package com.example.hostelbooking.services;

import com.example.hostelbooking.dto.BookingResponse;
import com.example.hostelbooking.entity.*;
import com.example.hostelbooking.repo.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingServices {

    private final BookingRepo bookingRepo;
    private final UserRepo userRepo;
    private final RoomRepo roomRepo;

    public BookingServices(BookingRepo bookingRepo,
                           UserRepo userRepo,
                           RoomRepo roomRepo) {
        this.bookingRepo = bookingRepo;
        this.userRepo = userRepo;
        this.roomRepo = roomRepo;
    }

    public Booking createBooking(Long userId, Long roomId, Booking booking) {

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Room room = roomRepo.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        booking.setUser(user);
        booking.setRoom(room);

        long days = (booking.getCheckOut().getTime() - booking.getCheckIn().getTime())
                / (1000 * 60 * 60 * 24);

        booking.setTotalAmount(days * room.getPrice());

        return bookingRepo.save(booking);
    }

    public List<BookingResponse> getAllBookings() {
        return bookingRepo.findAll().stream().map(b -> new BookingResponse(
                b.getBookingId(),
                b.getUser().getName(),
                b.getRoom().getRoomNumber(),
                b.getRoom().getRoomType(),
                b.getCheckIn(),
                b.getCheckOut(),
                b.getTotalAmount()
        )).collect(Collectors.toList());
    }
}