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

    //...code
    public Coupon createCoupon(Coupon request) {
        // Kiểm tra mã giảm giá đã tồn tại chưa
        if (couponRepository.existsByCode(request.getCode())) {
            throw new ValidationException("Mã giảm giá đã tồn tại trong hệ thống");
        }

        // Kiểm tra ngày hợp lệ
        if (request.getEndDate().isBefore(request.getStartDate())) {
            throw new ValidationException("Ngày kết thúc phải sau ngày bắt đầu");
        }

        request.setIsActive(true);  // Mặc định mã giảm giá được kích hoạt
        return couponRepository.save(request);
    }

// xóa
@Transactional
public void deleteCoupon(Integer id) {
    // Kiểm tra mã giảm giá có tồn tại không
    Coupon existingCoupon = couponRepository.findById(id)
            .orElseThrow(() -> new ValidationException("Mã giảm giá không tồn tại trong hệ thống"));

    // Xóa mã giảm giá
    couponRepository.delete(existingCoupon);
}
// Xóa nhiều mã giảm giá
@Transactional
public void deleteCoupons(List<Integer> ids) {
    // Kiểm tra danh sách có rỗng không
    if (ids == null || ids.isEmpty()) {
        throw new ValidationException("Danh sách mã giảm giá cần xóa không được để trống");
    }

    // Xóa tất cả mã giảm giá theo danh sách ID
    couponRepository.deleteAllById(ids);
}
}
