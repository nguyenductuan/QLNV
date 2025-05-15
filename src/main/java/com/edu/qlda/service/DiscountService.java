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

    private final DiscountRepository discountRepository;

    public DiscountService(DiscountRepository discountRepository) {
        this.discountRepository = discountRepository;
    }

    public List<Discount> getAllDiscounts() {
        return discountRepository.findAll();
    }

    /**
     * Apply percentage discount to total price.
     *
     * @param totalPrice Original total price
     * @param discountPercentage Discount percentage (0-100)
     * @return Price after discount
     */
    public Integer applyDiscount(Integer totalPrice, Integer discountPercentage) {
        if (discountPercentage == null || totalPrice == null) return totalPrice;
        return totalPrice - (totalPrice * discountPercentage) / 100;
    }

    public boolean isDiscountCodeExist(String code) {
        return discountRepository.existsByCode(code);
    }

    public Discount createCoupon(Discount request) {
        request.setCreateDate(LocalDate.now());
        request.setIsActive(1);
        return discountRepository.save(request);
    }

    public void updateCoupon(Discount request, Integer id) {
        Discount existingDiscount = discountRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Discount not found"));

        existingDiscount.setCode(request.getCode());
        existingDiscount.setDiscountname(request.getDiscountname());
        existingDiscount.setDiscountPercentage(request.getDiscountPercentage());
        existingDiscount.setUpdateDate(LocalDate.now());

        discountRepository.save(existingDiscount);
    }

    public void deleteCoupon(Integer id) {
        discountRepository.deleteById(id);
    }

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

    public Optional<Discount> getDiscountById(Integer id) {
        return discountRepository.findById(id);
    }
}
