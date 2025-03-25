package com.edu.qlda.controller;

import com.edu.qlda.dto.CartRequestDto;
import com.edu.qlda.entity.Cart;
import com.edu.qlda.playload.response.MessageResponse;
import com.edu.qlda.service.CartService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("http://localhost:4200")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    private static final String ACTION_SUCCESS = "Thành công";

    @PostMapping("/cart/add-product")
    public ResponseEntity<MessageResponse<Void>> addProductToCart(@RequestBody CartRequestDto request) {
        try {
            cartService.addProductToCart(request);
            MessageResponse<Void> response = new MessageResponse<>(200, ACTION_SUCCESS, "");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            MessageResponse<Void> response = new MessageResponse<>(500, "Lỗi", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/cart-view")
    public ResponseEntity<List<Cart>> getCart(@RequestParam Integer employeeId) {
        try {
            List<Cart> cart = cartService.getCart(employeeId);
            return ResponseEntity.ok(cart);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/cart/update-product")
    public ResponseEntity<String> updateCart(@RequestBody CartRequestDto request) {
        try {
            cartService.updateCart(request.getQuantity(), request.getEmployeeId(), request.getProductId());
            return ResponseEntity.ok(ACTION_SUCCESS);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi: " + e.getMessage());
        }
    }

    @DeleteMapping("/cart/delete-product/{id}")
    public ResponseEntity<String> deleteCart(@PathVariable Integer id) {
        try {
            cartService.deleteProduct(id);
            return ResponseEntity.ok(ACTION_SUCCESS);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi: " + e.getMessage());
        }
    }
}
