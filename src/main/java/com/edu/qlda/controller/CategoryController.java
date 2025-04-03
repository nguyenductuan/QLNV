package com.edu.qlda.controller;

import com.edu.qlda.entity.Category;

import com.edu.qlda.service.CategoryService;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @PostMapping
    public ResponseEntity<ProductCategory> createCategory(@Valid @RequestBody ProductCategory request) {
        ProductCategory category = categoryService.createCategory(request);
        return ResponseEntity.ok(category);
    }
    // Chỉnh sửa nhóm sản phẩm
    @PostMapping
    public ResponseEntity<ProductCategory> updateCategory(@Valid @RequestBody ProductCategory request) {
        ProductCategory category = categoryService.updateCategory(request);
        return ResponseEntity.ok(category);
    }
    // Xem chi tết nhóm sản phẩm
    @GetMapping("/{id}")
    public ResponseEntity<ProductCategory> getCategoryById(@PathVariable Integer id) {
        ProductCategory category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(category);
    }
    // Xóa nhóm sản phẩm
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Integer id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok("Nhóm sản phẩm đã được xóa thành công");
    }


}
