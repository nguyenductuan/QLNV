package com.edu.qlda.entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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
    @NotBlank(message = "Tên không được để trống")
    private  String name;
    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "^\\d{10,11}$", message = " Số điện thoại không hợp lệ")
    private String phone;
    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    private  String email;
    private  Integer status;
    private String address;
    private LocalDate createdate;
    private LocalDate updatedate;
    private String password;
    private Integer gender;
    private  LocalDate birthday;
    @ManyToOne
    @JoinColumn(name = "position_id",nullable = false)
    private Position position;
    @ManyToOne
    @JoinColumn(name = "role_id",nullable = false)
    private Role role;
}
