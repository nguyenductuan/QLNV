package com.edu.qlda.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JoinColumn(name = "product_id")
    private Integer productId;
    private String name;
    private Integer price;
    private Integer quantity;
    private String thumbnail;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

}
