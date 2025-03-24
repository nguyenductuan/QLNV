package com.edu.qlda.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderDto {
    @JsonProperty("user_id")
    private Integer userId;
    private  String name;
    private  String email;
    private  String phone;
    private  String address;
    @JsonProperty("total_amount")
    private Integer totalAmount;
    @JsonProperty("payment_status")
    private String 	paymentStatus;
    private String note;
    private List<CartItem> cartItem;
}
