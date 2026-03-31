package org.example.libraries.Service;

import org.example.libraries.Entity.User;
import org.example.libraries.Repo.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository repo;

    public UserService(UserRepository repo) {
        this.repo = repo;
    }

    public List<User> getAllUsers() {
        return repo.findAll();
    }

    public User addUser(User u) {
        return repo.save(u);
    }

    public User updateUser(int id, User u) {
        User exists = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        exists.setName(u.getName());
        exists.setPassword(u.getPassword());

        return repo.save(exists);
    }

    public String deleteUser(int id) {
        repo.deleteById(id);
        return "User deleted";
    }
}