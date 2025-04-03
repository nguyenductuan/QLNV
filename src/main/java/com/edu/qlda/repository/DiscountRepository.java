package com.edu.qlda.repository;

import com.edu.qlda.entity.Discount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiscountRepository  extends JpaRepository<Discount,Integer> {
    boolean existsByCode(String code);
}
