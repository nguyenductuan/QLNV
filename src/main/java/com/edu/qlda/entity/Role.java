package com.edu.qlda.entity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonCreator;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Set;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JoinColumn(name = "role_id")
    private Integer roleid;
    private String name;
    @OneToMany(mappedBy = "role")
    @JsonBackReference
    private Set<Employee> employeeList;



    @JsonCreator
    public Role(String roleId) {
        this.roleid = Math.toIntExact(Long.parseLong(roleId)); // Chuyển đổi chuỗi thành Long (hoặc Integer, tùy vào yêu cầu)
        this.name = "Role " + roleId; // Ví dụ, bạn có thể gán tên cho Role từ ID hoặc để trống
    }


}


