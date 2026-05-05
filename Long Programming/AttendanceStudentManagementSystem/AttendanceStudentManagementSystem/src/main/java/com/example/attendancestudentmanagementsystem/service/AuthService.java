package com.example.attendancestudentmanagementsystem.service;

import com.example.attendancestudentmanagementsystem.dto.JwtResponse;
import com.example.attendancestudentmanagementsystem.dto.LoginRequest;
import com.example.attendancestudentmanagementsystem.entity.User;
import com.example.attendancestudentmanagementsystem.repository.UserRepository;
import com.example.attendancestudentmanagementsystem.security.JwtUtil;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepo;

    public AuthService(AuthenticationManager authManager, JwtUtil jwtUtil, UserRepository userRepo) {
        this.authManager = authManager;
        this.jwtUtil = jwtUtil;
        this.userRepo = userRepo;
    }

    public JwtResponse login(LoginRequest request) {

        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        User user = userRepo.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Invalid username or password"));

        String token = jwtUtil.generateToken(
                user.getUsername(),
                user.getRole().name()
        );

        return new JwtResponse(token, user.getRole().name());
    }
}