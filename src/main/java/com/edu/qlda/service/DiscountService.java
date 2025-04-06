package com.edu.qlda.service;

import com.edu.qlda.entity.Discount;

import com.edu.qlda.repository.DiscountRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DiscountService {

    private DiscountRepository discountRepository;
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
    public boolean isDiscountCodeExist(String code) {
        return discountRepository.existsByCode(code);
    }
    public Discount createCoupon(Discount request) {
        request.setCreateDate(LocalDate.now());
        request.setIsActive(1);
        return discountRepository.save(request);
    }
    public void updateCoupon(Discount request, Integer id) {

        Optional<Discount> discount = discountRepository.findById(id);
        if (discount.isPresent()) {

            Discount existingDiscount = discount.get();
            existingDiscount.setCode(request.getCode());
            existingDiscount.setDiscountname(request.getDiscountname());
            existingDiscount.setDiscountPercentage(request.getDiscountPercentage());
            existingDiscount.setUpdateDate(LocalDate.now());
            discountRepository.save(existingDiscount);
        }
    }
// Xóa mã giảm giá
public void deleteCoupon(Integer id) {
discountRepository.deleteById(id);
}
// Xóa nhiều mã giảm giá
public List<Integer> deleteCoupons(List<Integer> ids) {
    List<Integer> notFoundIds = new ArrayList<>();
    for (Integer id : ids) {
        if (discountRepository.existsById(id)) {
            discountRepository.deleteById(id);
        } else {
            notFoundIds.add(id);
        }
    }
    return notFoundIds;
}
// lấy thông tin mã giảm giá khi có id
    public Optional<Discount> getDiscountByID(Integer id){
         return discountRepository.findById(id);
    }
}
