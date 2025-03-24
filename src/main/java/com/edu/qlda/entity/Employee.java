package com.edu.qlda.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "employee")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JoinColumn(name = "employee_id")
    private Integer employeeId;
    private  String name;
    private String phone;
    private  String email;
    private  Integer status;
    private String address;
    private LocalDate createdate;
    private LocalDate updatedate;
    private String password;
    private Integer gender;
    private  Date birthday;
    @ManyToOne
    @JoinColumn(name = "position_id",nullable = false)
    private Position position;
    @ManyToOne
    @JoinColumn(name = "role_id",nullable = false)
    private Role role;
}
