package com.edu.qlda.controller;

import com.edu.qlda.dto.CartrequestDto;
import com.edu.qlda.entity.Cart;

import com.edu.qlda.playload.response.Messageresponse;
import com.edu.qlda.service.CartService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@CrossOrigin("http://localhost:4200")

public class CartController {

    private final CartService cartService;
    public  CartController(CartService cartService){
        this.cartService=cartService;
    }

    private static final String ACTION_SUCCESS = "Thành cong";
    @PostMapping("cart/add-product")

    public  ResponseEntity<Messageresponse<Void>> addProductToCart(@RequestBody CartrequestDto request) {
        cartService.addProductToCart(request);
        Messageresponse <Void> response = new Messageresponse(200, ACTION_SUCCESS, "");// Trả về ResponseEntity với mã trạng thái HTTP là OK (200)
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @GetMapping("/cart-view") // API để lấy giỏ hàng của người dùng
    public List<Cart> getCart(Integer employeeId) {
        return cartService.getCart(employeeId);
    }
    @PostMapping("cart/updateproduct")
    public String updateCart(@RequestBody CartrequestDto request) {
        cartService.updateCart(request.getQuantity(),request.getEmployeeId(),request.getProductId());
        return ACTION_SUCCESS;
    }
    @DeleteMapping("delete-producttoCart/{id}")
    public String deleteCart(@PathVariable Integer id) {
        cartService.deleteproduct(id);
        return ACTION_SUCCESS;
    }
}