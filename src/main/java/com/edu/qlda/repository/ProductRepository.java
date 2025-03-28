package com.edu.qlda.repository;

import com.edu.qlda.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    Product findByName(String name);
    @Query(value =
            "select *  from products  where product_id =?", nativeQuery = true)
   Product findByIdProduct (Integer productId);
    @Query(value =
            "Select  * from products  WHERE product_id IN :productIds", nativeQuery = true)
List<Product> findProductsByIds(@Param("productIds") List<Integer> productIds);


}
