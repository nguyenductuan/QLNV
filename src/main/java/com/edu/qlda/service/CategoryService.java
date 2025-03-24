package com.edu.qlda.service;

import com.edu.qlda.entity.Category;
import com.edu.qlda.repository.CategoryRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final  CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }
    public List<Category> listcategorys() {
        return categoryRepository.findAll();
    }
}
