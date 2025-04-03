package com.edu.qlda.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
@JoinColumn(name = "category_id")
    @NotBlank(message = "Tên nhóm sản phẩm không được để trống")
    private Integer categoryId;
    private  String name;

    @JsonCreator
    public Category(Integer categoryId) {
        this.categoryId = Math.toIntExact((categoryId));
        this.name = "Category " + categoryId;
    }
}
