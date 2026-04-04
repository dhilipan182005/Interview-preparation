package com.example.hostelbooking.controller;

import com.example.hostelbooking.entity.Room;
import com.example.hostelbooking.services.RoomServices;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rooms")
public class RoomController {

    private final RoomServices roomServices;

    public RoomController(RoomServices roomServices) {
        this.roomServices = roomServices;
    }

    @PostMapping("/POSTROOM")
    public Room addRoom(@RequestBody Room room) {
        return roomServices.saveRoom(room);
    }

    @GetMapping("/GETROOM")
    public List<Room> getAllRooms() {
        return roomServices.getAllRooms();
    }
}