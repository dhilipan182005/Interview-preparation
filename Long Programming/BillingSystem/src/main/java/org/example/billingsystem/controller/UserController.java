package org.example.billingsystem.controller;

import org.example.billingsystem.entity.User;
import org.example.billingsystem.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*")

public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping("/api")
    public List<User> getUsers() {
        return service.getAllUsers();
    }

    @PostMapping("/postuser")
    public User addUser(@RequestBody User u) {
        return service.addUser(u);
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable int id, @RequestBody User u) {
        return service.updateUser(id, u);
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable int id) {
        return service.deleteUser(id);
    }
}