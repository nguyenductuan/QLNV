package com.edu.qlda.dto;

import com.edu.qlda.entity.Employee;
import lombok.Data;

@Data
public class LoginResponse {
    private String token;
    private  String message;
    private Employee employee;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
}
