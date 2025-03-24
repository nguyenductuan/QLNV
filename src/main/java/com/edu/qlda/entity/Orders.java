package com.edu.qlda.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "orders")
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JoinColumn(name = "order_id")
    private Integer orderId;
    @JsonProperty("user_id")
    private Integer userId;
    private  String name;
    private Date createdate;
    private  String email;
    private  String phone;
    private  String address;
    @JsonProperty("total_amount")
    private Integer totalAmount;
    @JsonProperty("payment_status")
    private String 	paymentStatus;
    // thieu phuong thuc, note, thowi gian dat hang
}
