package com.edu.qlda.controller;

import com.edu.qlda.dto.LoginRequest;
import com.edu.qlda.dto.LoginResponse;
import com.edu.qlda.jwt.JwtUtils;
import com.edu.qlda.service.EmployeeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("http://localhost:4200")
public class AuthController {

@Autowired
private EmployeeService employeeService;
    @Autowired
    private JwtUtils jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        if (employeeService.validateUsers(request.getUsername(), request.getPassword())) {
            String token = jwtUtil.generateToken(request.getUsername());

            return ResponseEntity.ok(new LoginResponse(token));
        } else {
            return ResponseEntity.status(401).body("Invalid username or password");
        }
    }
}
