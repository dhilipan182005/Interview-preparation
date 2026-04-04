package com.example.hostelbooking.repo;

import com.example.hostelbooking.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long> {
}