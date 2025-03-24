package com.edu.qlda.repository;

import com.edu.qlda.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailRepository  extends JpaRepository<OrderDetail, Integer> {
}
