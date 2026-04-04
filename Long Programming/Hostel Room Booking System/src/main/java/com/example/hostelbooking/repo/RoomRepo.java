package com.example.hostelbooking.repo;

import com.example.hostelbooking.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepo extends JpaRepository<Room, Long> {
}