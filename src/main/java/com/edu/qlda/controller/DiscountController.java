package com.edu.qlda.controller;

import com.edu.qlda.dto.DiscountDto;
import com.edu.qlda.entity.Discount;
import com.edu.qlda.playload.response.response.Messageresponse;
import com.edu.qlda.service.DiscountService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/discounts")
@CrossOrigin("http://localhost:4200")
public class DiscountController {

    private final DiscountService discountService;

    public DiscountController(DiscountService discountService) {
        this.discountService = discountService;
    }

    @GetMapping
    public ResponseEntity<List<Discount>> getAllDiscounts() {
        return ResponseEntity.ok(discountService.getAllDiscounts());
    }

    @PostMapping("/apply")
    public ResponseEntity<Integer> applyDiscount(@RequestBody DiscountDto request) {
        Integer discountedAmount = discountService.applyDiscount(request.getTotalAmount(), request.getSelectedDiscount());
        return ResponseEntity.ok(discountedAmount);
    }

    @PostMapping
    public ResponseEntity<Messageresponse<Void>> createDiscount(@Valid @RequestBody Discount request,
                                                                BindingResult bindingResult) {
        ResponseEntity<Messageresponse<Void>> errorResponse = handleValidationErrors(bindingResult);
        if (errorResponse != null) return errorResponse;

        try {
            discountService.createCoupon(request);
            return ResponseEntity.ok(new Messageresponse<>(200, "Thêm mới mã giảm giá thành công"));
        } catch (Exception e) {
            return handleException(e);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Messageresponse<Void>> updateDiscount(@Valid @RequestBody Discount request,
                                                                BindingResult bindingResult, @PathVariable Integer id) {
        ResponseEntity<Messageresponse<Void>> errorResponse = handleValidationErrors(bindingResult);
        if (errorResponse != null) return errorResponse;

        Optional<Discount> discount = discountService.getDiscountById(id);
        if (discount.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new Messageresponse<>(404, "Mã giảm giá không tồn tại trong hệ thống"));
        }

        try {
            discountService.updateCoupon(request, id);
            return ResponseEntity.ok(new Messageresponse<>(200, "Cập nhật mã giảm giá thành công"));
        } catch (Exception e) {
            return handleException(e);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Messageresponse<Void>> deleteDiscount(@PathVariable Integer id) {
        Optional<Discount> discount = discountService.getDiscountById(id);
        if (discount.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new Messageresponse<>(404, "Mã giảm giá không tồn tại trong hệ thống"));
        }

        try {
            discountService.deleteCoupon(id);
            return ResponseEntity.ok(new Messageresponse<>(200, "Xóa mã giảm giá thành công"));
        } catch (Exception e) {
            return handleException(e);
        }
    }

    @DeleteMapping
    public ResponseEntity<Messageresponse<List<Integer>>> deleteDiscounts(@RequestBody List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new Messageresponse<>(400, "Danh sách mã giảm giá cần xóa không được để trống"));
        }


            List<Integer> notFoundIds = discountService.deleteCoupons(ids);
            if (notFoundIds.isEmpty()) {
                return ResponseEntity.ok(new Messageresponse<>(200, "Xóa mã giảm giá thành công"));
            } else {
                return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                        .body(new Messageresponse<>(206, "Một số mã giảm giá không tồn tại", notFoundIds));
            }

    }

    // === PRIVATE METHODS ===

    private ResponseEntity<Messageresponse<Void>> handleValidationErrors(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String message = bindingResult.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage)
                    .collect(Collectors.joining(", "));
            return ResponseEntity.badRequest().body(new Messageresponse<>(400, message));
        }
        return null;
    }

    private ResponseEntity<Messageresponse<Void>> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new Messageresponse<>(500, "Lỗi hệ thống: " + e.getMessage()));
    }
}
