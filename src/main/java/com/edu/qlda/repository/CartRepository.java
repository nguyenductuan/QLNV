package com.edu.qlda.repository;


import com.edu.qlda.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart,Integer> {
    @Query(value = "Select  c.cart_id, c.quantity,p.name, p.price, p.product_id,c.employee_id " +
            "FROM products p inner join cart c on p.product_id = c.product_id " +
            "inner join employee e on c.employee_id = e.employee_id" +
            " WHERE c.employee_id=?", nativeQuery = true)
    List<Cart> findByEmployee(Integer employeeId); // Tìm giỏ hàng của người dùng
    @Transactional
    @Modifying
    @Query(value = "update cart as c set c.quantity =?1  where c.employee_id = ?2 and c.product_id = ?3", nativeQuery = true)
    void updatecart(Integer quantity, Integer employeeId, Integer productId);
    @Transactional
    @Modifying
    @Query(value = "DELETE FROM cart where product_id = ?", nativeQuery = true)
    void deleteproduct(Integer productId);
    @Transactional
    @Modifying
    @Query(value = "INSERT INTO `cart` (`employee_id`,`product_id`,`quantity`) VALUES (:employee_id,:product_id,:quantity)", nativeQuery = true)
    void  savecart(
            @Param("employee_id") Integer employeeId,
            @Param("product_id") Integer productId,
              @Param("quantity") Integer quantity

    );
    @Query(value = "Select cart_id, product_id, quantity, employee_id  From cart where employee_id = ?1 and product_id =?2 ", nativeQuery = true)
    Optional<Cart> findByCartIdAndProductId ( Integer employeeId, Integer productId);

}

