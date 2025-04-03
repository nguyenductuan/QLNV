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

    // Thêm mã giảm giá
    @PostMapping
    public ResponseEntity<Coupon> createCoupon(@Valid @RequestBody Coupon request) {
        Coupon coupon = couponService.createCoupon(request);
        return ResponseEntity.ok(coupon);
    }
    // Chi tiết mã giảm giá

    // Xóa mã giảm giá
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCoupon(@PathVariable Integer id) {
        couponService.deleteCoupon(id);
        return ResponseEntity.ok("Mã giảm giá đã được xóa thành công");
    }
    // Xóa nhiều mã giảm giá
    @DeleteMapping
    public ResponseEntity<String> deleteCoupons(@RequestBody List<Integer> ids) {
        couponService.deleteCoupons(ids);
        return ResponseEntity.ok("Các mã giảm giá đã được xóa thành công");
    }
}
