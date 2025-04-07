package com.edu.qlda.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "discount")
public class Discount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "discount_id")
    private Integer discountid;
    @NotBlank(message = "Mã code không được để trống")
    private String code;
    @NotBlank(message = "Nội dung mã giảm giá không được để trống")
    private String discountname;

    private Integer discountPercentage;
    private Integer isActive;
    LocalDate createDate;
    LocalDate updateDate;

}

