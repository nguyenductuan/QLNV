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

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    private static final String ACTION_SUCCESS = "Thành công";

    @PostMapping("cart/add-product")
    public ResponseEntity<Messageresponse<Void>> addProductToCart(@RequestBody CartrequestDto request) {


        cartService.addProductToCart(request);
        return ResponseEntity.ok(new Messageresponse<>(200, ACTION_SUCCESS));
    }

    @GetMapping("/cart-view") // API để lấy giỏ hàng của người dùng
    public ResponseEntity<Messageresponse<List<Cart>>> getCart(Integer employeeId) {
        List<Cart> cartItems = cartService.getCart(employeeId);
        return ResponseEntity.ok(new Messageresponse<>(200, ACTION_SUCCESS, cartItems));
    }

    @PostMapping("cart/updateproduct")
    public ResponseEntity<Messageresponse<Void>> updateCart(@RequestBody CartrequestDto request) {
        cartService.updateCart(request.getQuantity(), request.getEmployeeId(), request.getProductId());
        return ResponseEntity.ok(new Messageresponse<>(200, ACTION_SUCCESS));
    }

    @DeleteMapping("delete-producttoCart/{id}")
    public ResponseEntity<Messageresponse<Void>> deleteCart(@PathVariable Integer id) {
        cartService.deleteproduct(id);
        return ResponseEntity.ok(new Messageresponse<>(200, ACTION_SUCCESS));
    }

    // Xóa nhiều sản phẩm trong giỏ hàng cần sửa
    @DeleteMapping("delete-producttoCart/[ids]")
    public ResponseEntity<Messageresponse<List<Integer>>> deleteCartItems(@RequestBody List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            return ResponseEntity.badRequest().body(new Messageresponse<>(404, "Danh sách mã giảm giá cần xóa không được để trống"));
        }
        try {

            // danh sách mã giảm giá cần xóa
            List<Integer> notFoundIds = cartService.deleteProducts(ids);
            if (notFoundIds.isEmpty()) {
                return ResponseEntity.ok(new Messageresponse<>(200, "Các mã giảm giá đã được xóa thành công"));
            } else {
                return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body(new Messageresponse<>(206, "Một số mã giảm giá không tồn tại", notFoundIds));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Messageresponse<>(500, "Lỗi hệ thống:" + e.getMessage()));
        }
    }
}