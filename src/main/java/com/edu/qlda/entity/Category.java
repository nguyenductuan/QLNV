package com.edu.qlda.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.persistence.*;
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
    private Integer categoryId;
    private  String name;

    @JsonCreator
    public Category(Integer categoryId) {
        this.categoryId = Math.toIntExact((categoryId));
        this.name = "Category " + categoryId;
    }
}
