package com.edu.qlda.controller;

import com.edu.qlda.entity.Category;

import com.edu.qlda.service.CategoryService;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
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

}
