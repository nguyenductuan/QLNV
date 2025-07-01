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
            "SELECT c.cart_id , c.employee_id, c.product_id, p.category_id, p.name, p.price, p.createdate,p.status,p.thumbnail,p.updatedate, c.quantity from cart c INNER JOIN products p ON  c.product_id = p.product_id WHERE c.product_id IN :productIds", nativeQuery = true)
List<Product> findProductsByIds(@Param("productIds") List<Integer> productIds);

}
