package com.edu.qlda.service;

import com.edu.qlda.dto.CartrequestDto;
import com.edu.qlda.entity.Cart;
import com.edu.qlda.repository.CartRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {
    private final CartRepository cartRepository;

    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }
    public void addProductToCart(CartrequestDto request) {
        //Kiểm tra sản pẩm có trong giỏ hang
        Optional<Cart> cartItemOpt = cartRepository.findByCartIdAndProductId(request.getEmployeeId(), request.getProductId());
        if (cartItemOpt.isPresent()) {
//            //Nếu sản phẩm có trong giorhangf thì tăng số lượng

            int quantity2 = cartItemOpt.get().getQuantity() + request.getQuantity();
            cartRepository.updatecart(quantity2, request.getEmployeeId(), request.getProductId());
        } else {
            //Nếu sản phẩm không có giỏ hàng thì thêm mới
            cartRepository.savecart(request.getEmployeeId(), request.getProductId(), request.getQuantity());
        }
    }
    public void updateCart(Integer quantity, Integer employeeId, Integer productId) {
        cartRepository.updatecart(quantity, employeeId, productId);
    }
    public List<Cart> getCart(Integer employeeId) {

        return cartRepository.findByEmployee(employeeId);
    }
    public void deleteproduct(Integer productId) {
        cartRepository.deleteproduct(productId);
    }
}
