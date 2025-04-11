package com.edu.qlda.controller;

import com.edu.qlda.entity.Category;


import com.edu.qlda.playload.response.Messageresponse;
import com.edu.qlda.service.CategoryService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("http://localhost:4200")
public class CategoryController {
    private final  CategoryService categoryService;
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }
    @GetMapping("/category")
    public List<Category> getAllCategorys() {
        return categoryService.listcategorys();
    }
    // Thêm mới nhóm sản phẩm
    @PostMapping("/addcategory")
    public ResponseEntity<Category> createCategory(@Valid @RequestBody Category request) {
        Category category = categoryService.createCategory(request);
        return ResponseEntity.ok(category);
    }
    // Chỉnh sửa nhóm sản phẩm
    @PostMapping("update-category/{id}")
    public ResponseEntity<Category> updateCategory(@Valid @RequestBody Category request, @PathVariable Integer id) {
        Category category = categoryService.updateCategory(id,request);
        return ResponseEntity.ok(category);
    }
    // Xem chi tết nhóm sản phẩm
    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Integer id) {
        Category category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(category);
    }
    // Xóa nhóm sản phẩm
    @DeleteMapping("delete-category/{id}")
    public ResponseEntity<Messageresponse<Category>> deleteCategory(@PathVariable Integer id) {
        categoryService.deleteCategory(id);
        Messageresponse<Category> response = new Messageresponse<>(200,"Xóa nhóm sản phẩm thành công");
        return ResponseEntity.ok(response);

    }
    // Xóa nhiều nhóm sản phẩm

    @DeleteMapping("delete-categorys")
    public ResponseEntity<Messageresponse<List<Integer>>> deleteCategorys(@RequestBody List<Integer> ids) {
        try {
            // Kiểm tra danh sách có rỗng không
            if (ids == null || ids.isEmpty()) {
                return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body(new Messageresponse<>(404, "Danh sách mã giảm giá cần xóa không được để trống"));
            }
            // danh sách mã giảm giá cần xóa
            List<Integer> notFoundIds = categoryService.deleteCategorys(ids);
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
