package com.example.hostelbooking.controller;

import com.example.hostelbooking.entity.User;
import com.example.hostelbooking.services.UserServices;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserServices userServices;

    public UserController(UserServices userServices) {
        this.userServices = userServices;
    }

    @PostMapping("/postuser")
    public User createUser(@RequestBody User user) {
        return userServices.saveUser(user);
    }

    @GetMapping("/getuser")
    public List<User> getAllUsers() {
        return userServices.getAllUsers();
    }
}