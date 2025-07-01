package com.edu.qlda.service;

import com.edu.qlda.entity.Category;
import com.edu.qlda.exception.ValidationException;
import com.edu.qlda.repository.CategoryRepository;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService {

    private static final String CATEGORY_NOT_FOUND = "Category not found in the system";
    private static final String CATEGORY_NAME_ALREADY_EXISTS = "Category name already exists";

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category createCategory(Category request) {
        if (categoryRepository.existsByName(request.getName())) {
            throw new ValidationException(CATEGORY_NAME_ALREADY_EXISTS);
        }
        return categoryRepository.save(request);
    }

    public Category updateCategory(Integer id, Category request) {
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new ValidationException(CATEGORY_NOT_FOUND));

        if (!existingCategory.getName().equals(request.getName())
                && categoryRepository.existsByName(request.getName())) {
            throw new ValidationException(CATEGORY_NAME_ALREADY_EXISTS);
        }

        existingCategory.setName(request.getName());
        return categoryRepository.save(existingCategory);
    }

    public void deleteCategory(Integer id) {
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new ValidationException(CATEGORY_NOT_FOUND));

        categoryRepository.delete(existingCategory);
    }

    public List<Integer> deleteCategories(List<Integer> ids) {
        List<Integer> notFoundIds = new ArrayList<>();
        for (Integer id : ids) {
            if (categoryRepository.existsById(id)) {
                categoryRepository.deleteById(id);
            } else {
                notFoundIds.add(id);
            }
        }
        return notFoundIds;
    }

    public Category getCategoryById(Integer id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ValidationException(CATEGORY_NOT_FOUND));
    }
}
