package com.edu.qlda.service;

import com.edu.qlda.entity.Discount;
import com.edu.qlda.repository.DiscountRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiscountService {

    private final DiscountRepository discountRepository;
    public DiscountService(DiscountRepository discountRepository) {
        this.discountRepository = discountRepository;
    }
    public List<Discount> listDiscount(){
     return  discountRepository.findAll();
    }
    // có thể thêm  hàm xử lý lấy ra % giảm giá từ discount code
    public Integer appDiscount (Integer totalPrice, Integer discount){
       return  totalPrice - (totalPrice * discount)/100;
    }
}
