package com.edu.qlda.service;

import com.edu.qlda.dto.ProductDto;
import com.edu.qlda.entity.Category;
import com.edu.qlda.entity.Product;
import com.edu.qlda.repository.CategoryRepository;
import com.edu.qlda.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    public List<Product> listProducts() {
        return productRepository.findAll();
    }

    public List<Product> findProductsByIds(List<Integer> productIds) {
        return productRepository.findProductsByIds(productIds);
    }

    public Product getProductById(Integer productId) {
        return productRepository.findByIdProduct(productId);
    }

    public Product createProduct(ProductDto productDto, String filename) {
        Category category = categoryRepository.findById(productDto.getCategoryid())
                .orElseThrow(() -> new RuntimeException("Danh mục sản phẩm không tồn tại"));

        Product product = new Product();
        product.setName(productDto.getName());
        product.setCategory(category);
        product.setStatus(productDto.getStatus());
        product.setCreatedate(LocalDate.now());
        product.setPrice(productDto.getPrice());
        product.setThumbnail(filename);
        product.setQuantity(productDto.getQuantity());

        return productRepository.save(product);
    }

    public Product updateProduct(ProductDto productDto, Integer id, String filename) {
        Product product = getProductById(id);
        if (product == null) {
            throw new RuntimeException("Sản phẩm không tồn tại");
        }

        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice());
        if (filename != null) {
            product.setThumbnail(filename);
        }
        product.setUpdatedate(LocalDate.now());
        product.setQuantity(productDto.getQuantity());

        return productRepository.save(product);
    }

    public void deleteProduct(Integer id) {
        productRepository.deleteById(id);
    }

    public List<Integer> deleteProducts(List<Integer> ids) {
        List<Integer> notFoundIds = new ArrayList<>();
        for (Integer id : ids) {
            if (productRepository.existsById(id)) {
                productRepository.deleteById(id);
            } else {
                notFoundIds.add(id);
            }
        }
        return notFoundIds;
    }
}
