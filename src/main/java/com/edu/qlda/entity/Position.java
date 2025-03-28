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
@Entity(name = "position")
public class Position {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JoinColumn(name = "position_id")
    private Integer positionId;
    private String name;
    @OneToMany(mappedBy = "position")
    @JsonBackReference
    private Set<Employee> employeeList;

    @JsonCreator
    public Position(Integer positionid) {
        this.positionId = Math.toIntExact((positionid));
        this.name = "Position " + positionid; // Ví dụ, bạn có thể gán tên cho Role từ ID hoặc để trống
    }
}