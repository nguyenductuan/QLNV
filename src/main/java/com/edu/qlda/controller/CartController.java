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
@RequestMapping("/cart")
@CrossOrigin("http://localhost:4200")
public class CartController {

    private final CartService cartService;
    private static final String ACTION_SUCCESS = "Thành công";

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/add")
    public ResponseEntity<Messageresponse<Void>> addProductToCart(@RequestBody CartrequestDto request) {
        cartService.addProductToCart(request);
        return ResponseEntity.ok(new Messageresponse<>(200, ACTION_SUCCESS));
    }

    @GetMapping("/view/{employeeId}")
    public ResponseEntity<Messageresponse<List<Cart>>> getCart(@PathVariable Integer employeeId) {
        List<Cart> cartItems = cartService.getCart(employeeId);
        return ResponseEntity.ok(new Messageresponse<>(200, ACTION_SUCCESS, cartItems));
    }

    @PostMapping("/update")
    public ResponseEntity<Messageresponse<Void>> updateCart(@RequestBody CartrequestDto request) {
        cartService.updateCart(request.getQuantity(), request.getEmployeeId(), request.getProductId());
        return ResponseEntity.ok(new Messageresponse<>(200, ACTION_SUCCESS));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Messageresponse<Void>> deleteCartItem(@PathVariable Integer id) {
        cartService.deleteProduct(id);
        return ResponseEntity.ok(new Messageresponse<>(200, ACTION_SUCCESS));
    }

    @DeleteMapping("/delete-multiple")
    public ResponseEntity<Messageresponse<List<Integer>>> deleteCartItems(@RequestBody List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            return ResponseEntity.badRequest().body(
                    new Messageresponse<>(404, "Danh sách sản phẩm cần xóa không được để trống"));
        }

        try {
            List<Integer> notFoundIds = cartService.deleteProducts(ids);
            if (notFoundIds.isEmpty()) {
                return ResponseEntity.ok(new Messageresponse<>(200, "Xóa sản phẩm thành công"));
            } else {
                return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                        .body(new Messageresponse<>(206, "Một số sản phẩm không tồn tại", notFoundIds));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Messageresponse<>(500, "Lỗi hệ thống: " + e.getMessage()));
        }
    }
}
