package com.edu.qlda.service;

import com.edu.qlda.entity.Category;
import com.edu.qlda.exception.ValidationException;
import com.edu.qlda.repository.CategoryRepository;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService {
    private static final String CATEGORY_NOT_FOUND = "Nhóm sản phẩm không tồn tại trong hệ thống";
    private final  CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }
    public List<Category> listcategorys() {
        return categoryRepository.findAll();
    }
    public Category createCategory(Category request) {
        if (categoryRepository.existsByName(request.getName())) {
            throw new ValidationException(CATEGORY_NOT_FOUND);
        }
        return categoryRepository.save(request);
    }
    public Category updateCategory(Integer id, Category request) {
        // Kiểm tra nhóm sản phẩm có tồn tại không
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new ValidationException(CATEGORY_NOT_FOUND));
        // Kiểm tra xem tên mới có bị trùng không (trừ chính nó)
        if (!existingCategory.getName().equals(request.getName()) && categoryRepository.existsByName(request.getName())) {
            throw new ValidationException(CATEGORY_NOT_FOUND);
        }
        // Cập nhật thông tin
        existingCategory.setName(request.getName());
        return categoryRepository.save(existingCategory);
    }
    public void deleteCategory(Integer id) {
        // Kiểm tra nhóm sản phẩm có tồn tại không
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new ValidationException(CATEGORY_NOT_FOUND));
        // Xóa nhóm sản phẩm
        categoryRepository.delete(existingCategory);
    }
    // Xóa nhiều mã giảm giá
    public List<Integer> deleteCategorys(List<Integer> ids) {
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
