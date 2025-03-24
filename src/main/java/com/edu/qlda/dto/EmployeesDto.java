package com.edu.qlda.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.time.LocalDate;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeesDto {
    private String name;
    private String phone;
    private String email;
    private Integer status;
    private Integer role;
    private Integer position;
    private String address;
    private LocalDate createdate;
    private LocalDate updatedate;
    private String password;
    private Integer gender;
    private Date birthday;
}
