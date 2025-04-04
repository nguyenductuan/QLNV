package com.edu.qlda.dto;

import com.edu.qlda.entity.Position;
import com.edu.qlda.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDto {
    String name;
    String phone;
    String email;
    String status;
    String address;
    Integer gender;
    String birthday;
    String password;
    Position position;
    Role role;
}
