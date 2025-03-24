package com.edu.qlda.controller;

import com.edu.qlda.dto.DiscountDto;
import com.edu.qlda.entity.Discount;

import com.edu.qlda.service.DiscountService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("http://localhost:4200")
public class DiscountController {

public final DiscountService discountService;

public DiscountController(DiscountService discountService) {
    this.discountService = discountService;
}
    @GetMapping("/discount")
    public List<Discount> getAllDiscount() {
        return discountService.listDiscount();
    }

    @PostMapping("/applydiscount")
    public Integer applyDiscount(@RequestBody DiscountDto request){
     return  discountService.appDiscount(request.getTotalAmount(), request.getSelectedDiscount());

    }
}
