package com.edu.qlda.controller;

import com.edu.qlda.dto.LoginResponse;
import com.edu.qlda.dto.Loginrequest;
import com.edu.qlda.entity.Employee;
import com.edu.qlda.jwt.JwtUtils;
import com.edu.qlda.service.EmployeeService;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("http://localhost:4200")
public class AuthController {
    private final EmployeeService employeeService;

    private final JwtUtils jwtUtil;

    public AuthController(JwtUtils jwtUtil, EmployeeService employeeService) {
        this.jwtUtil = jwtUtil;
        this.employeeService = employeeService;

    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody Loginrequest request) {

        if (employeeService.validateUsers(request.getUsername(), request.getPassword())) {
            String token = jwtUtil.generateToken(request.getUsername());
            Employee user = employeeService.getAccountByEmail(request.getUsername());
            Employee userDto = new Employee();
            BeanUtils.copyProperties(user, userDto);
            LoginResponse response = new LoginResponse();
            response.setToken(token);
            response.setEmployee(userDto);
            response.setMessage("Thành công");
            return ResponseEntity.ok(response);
        } else {
            LoginResponse response = new LoginResponse();
            response.setMessage("Invalid username or passwords");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }
}
