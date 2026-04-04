package com.example.hostelbooking.services;

import com.example.hostelbooking.entity.Room;
import com.example.hostelbooking.repo.RoomRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomServices {

    private final RoomRepo roomRepo;

    public RoomServices(RoomRepo roomRepo) {
        this.roomRepo = roomRepo;
    }

    public Room saveRoom(Room room) {
        return roomRepo.save(room);
    }

    public List<Room> getAllRooms() {
        return roomRepo.findAll();
    }
}