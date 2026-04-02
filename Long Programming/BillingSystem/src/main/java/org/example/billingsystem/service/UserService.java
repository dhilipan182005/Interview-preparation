package org.example.billingsystem.service;

import org.example.billingsystem.entity.User;
import org.example.billingsystem.repo.UserRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepo repo;

    public UserService(UserRepo repo) {
        this.repo = repo;
    }

    public List<User> getAllUsers() {
        return repo.findAll();
    }

    public User addUser(User u) {
        return repo.save(u);
    }

    public User updateUser(int id, User u) {
        return repo.findById((long) id).map(existing -> {
            existing.setName(u.getName());
            existing.setD_T(u.getD_T());
            return repo.save(existing);
        }).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public String deleteUser(int id) {
        repo.deleteById((long) id);
        return "Deleted successfully";
    }
}