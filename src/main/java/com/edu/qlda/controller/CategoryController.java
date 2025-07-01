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
@RequestMapping("/categories")
@CrossOrigin("http://localhost:4200")
public class CategoryController {

    private final CategoryService categoryService;
    private static final String ACTION_SUCCESS = "Thành công";

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @PostMapping
    public ResponseEntity<Category> createCategory(@Valid @RequestBody Category request) {
        Category category = categoryService.createCategory(request);
        return ResponseEntity.ok(category);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Category> updateCategory(@Valid @RequestBody Category request, @PathVariable Integer id) {
        Category category = categoryService.updateCategory(id, request);
        return ResponseEntity.ok(category);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Integer id) {
        Category category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(category);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Messageresponse<Void>> deleteCategory(@PathVariable Integer id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok(new Messageresponse<>(200, "Xóa nhóm sản phẩm thành công"));
    }

    @DeleteMapping
    public ResponseEntity<Messageresponse<List<Integer>>> deleteMultipleCategories(@RequestBody List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Messageresponse<>(400, "Danh sách nhóm sản phẩm cần xóa không được để trống"));
        }

        try {
            List<Integer> notFoundIds = categoryService.deleteCategories(ids);
            if (notFoundIds.isEmpty()) {
                return ResponseEntity.ok(new Messageresponse<>(200, "Xóa nhóm sản phẩm thành công"));
            } else {
                return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                        .body(new Messageresponse<>(206, "Một số nhóm sản phẩm không tồn tại", notFoundIds));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Messageresponse<>(500, "Lỗi hệ thống: " + e.getMessage()));
        }
    }
}
