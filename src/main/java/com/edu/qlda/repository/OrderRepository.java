package com.edu.qlda.repository;


import com.edu.qlda.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;




public interface OrderRepository extends JpaRepository<Orders, Integer> {

}
