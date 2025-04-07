package com.edu.qlda.controller;

import com.edu.qlda.dto.DiscountDto;
import com.edu.qlda.entity.Discount;


import com.edu.qlda.playload.response.Messageresponse;
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
@CrossOrigin("http://localhost:4200")
public class DiscountController {

    public final DiscountService discountService;

    public DiscountController(DiscountService discountService) {
        this.discountService = discountService;
    }
    // Phương thức xử lý chung
    private ResponseEntity<Messageresponse<Discount>> handleValidationErrors(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String message = bindingResult.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage)
                    .collect(Collectors.joining(", "));
            return ResponseEntity.ok(new Messageresponse<>(201, message));
        }
        return null;
    }
    @GetMapping("/discount")
    public List<Discount> getAllDiscount() {
        return discountService.listDiscount();
    }

    @PostMapping("/applydiscount")
    public Integer applyDiscount(@RequestBody DiscountDto request) {
        return discountService.appDiscount(request.getTotalAmount(), request.getSelectedDiscount());

    }
    private ResponseEntity<Messageresponse<Discount>> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new Messageresponse<>(409, e.getMessage()));
    }
    // Thêm mã giảm giá
    @PostMapping("/creatediscount")
    public ResponseEntity<Messageresponse<Discount>> createCoupon(@Valid @RequestBody Discount request ,
                                                                  BindingResult bindingResult) {
        try {
            ResponseEntity<Messageresponse<Discount>> errorResponse = handleValidationErrors(bindingResult);
            if (errorResponse != null)
                return errorResponse;
            discountService.createCoupon(request);
            return ResponseEntity.ok(new Messageresponse<>(200, "Thêm mới mã giảm giá thành công"));
        }
        catch (Exception e) {
            return handleException(e);
        }

    }
   // Cập nhật mã giảm giá
    @PutMapping("/updatediscount/{id}")
    public ResponseEntity<Messageresponse<Discount>> updatediscount(@Valid @RequestBody Discount request ,
                                                                    BindingResult bindingResult,@PathVariable Integer id) {
        try {
            ResponseEntity<Messageresponse<Discount>> errorResponse = handleValidationErrors(bindingResult);
            if (errorResponse != null) return errorResponse;

            Optional<Discount> discount = discountService.getDiscountByID(id);
            if (!discount.isPresent()) {
                return ResponseEntity.ok(new Messageresponse<>(201, "Cập nhật mã giảm giá thất bại do mã giảm giá không có trong hệ thống"));
            }
            discountService.updateCoupon(request, id);
            return ResponseEntity.ok(new Messageresponse<>(200, "Cập nhật mã giảm giá thành công"));
        }
        catch (Exception e) {
            return handleException(e);
        }

    }

    @DeleteMapping("delete-discount/")
    public ResponseEntity<Messageresponse<List<Integer>>> missingIds() {
        return  ResponseEntity.badRequest().body(new Messageresponse<>(400, "Phải truyền ID để xóa"));
    }
    // Xóa mã giảm giá
    @DeleteMapping("delete-discount/{id}")
    public ResponseEntity<Messageresponse<String>> deleteCoupon(@PathVariable Integer id) {
        try {
            Optional<Discount> existingCoupon = discountService.getDiscountByID(id);
            if (id == null || existingCoupon.isEmpty() ){
                return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body(new Messageresponse<>(404, "Mã giảm giá không tồn tại trong hệ thống"));
            }
            else {
                discountService.deleteCoupon(id);
                return ResponseEntity.status(HttpStatus.CREATED).body(new Messageresponse<>(200, "Xóa mã giảm giá thành công"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Messageresponse<>(500, "Lỗi hệ thống:" + e.getMessage()));
        }
    }
    // Xóa nhiều mã giảm giá
    @DeleteMapping("delete-discounts")
    public ResponseEntity<Messageresponse<List<Integer>>> deleteCoupons(@RequestBody List<Integer> ids) {
        try {
            // Kiểm tra danh sách có rỗng không
            if (ids == null || ids.isEmpty()) {
                return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body(new Messageresponse<>(404, "Danh sách mã giảm giá cần xóa không được để trống"));
            }
            // danh sách mã giảm giá cần xóa
            List<Integer> notFoundIds = discountService.deleteCoupons(ids);
            if (notFoundIds.isEmpty()) {
                return ResponseEntity.ok(new Messageresponse<>(200, "Các mã giảm giá đã được xóa thành công"));
            } else {
                return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body(new Messageresponse<>(404, "Một số mã giảm giá không tồn tại", notFoundIds));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Messageresponse<>(500, "Lỗi hệ thống:" + e.getMessage()));
        }
    }
}
