package com.edu.qlda.service;

import com.edu.qlda.dto.ProductDto;
import com.edu.qlda.entity.Category;
import com.edu.qlda.entity.Product;
import com.edu.qlda.repository.CategoryRepository;
import com.edu.qlda.repository.ProductRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    public  ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }
    public List<Product> listproducts(){
        return  productRepository.findAll();
    }
    public List<Product> findProductsByIds( List<Integer> productIds){
        return productRepository.findProductsByIds(productIds);
    }
    public Product productById(Integer  productId){
        return  productRepository.findByIdProduct(productId);
}
    public Product createproduct(ProductDto productDto, String filename){
       Category exitcategory =  categoryRepository.findById(productDto.getCategoryid())
               .orElseThrow(()->new RuntimeException("Danh mục sản phẩm không tồn tại"));
       Product product= new Product();
       product.setName(productDto.getName());
       product.setCategory(exitcategory);
       product.setStatus(productDto.getStatus());
       product.setCreatedate(LocalDate.now());
       product.setPrice(productDto.getPrice());
       product.setThumbnail(filename);
       product.setQuantity(productDto.getQuantity());
       return productRepository.save(product);
}
    public Product updateproduct(ProductDto productDto, Integer id, String filename){
        Product existingProductOpt = productById(id);
        existingProductOpt.setName(productDto.getName());
        existingProductOpt.setPrice(productDto.getPrice());
        existingProductOpt.setThumbnail(filename);
        existingProductOpt.setUpdatedate(LocalDate.now());
        existingProductOpt.setQuantity(productDto.getQuantity());
        return productRepository.save(existingProductOpt);
    }
    public void deleteproduct(Integer id){
        productRepository.deleteById(id);
    }
// xóa nhiều sản phẩm
@Transactional
public void deleteProducts(List<Integer> productIds) {
    // Kiểm tra danh sách có rỗng không
    if (productIds == null || productIds.isEmpty()) {
        throw new ValidationException("Danh sách sản phẩm cần xóa không được để trống");
    }

    // Kiểm tra xem có ID nào không tồn tại không
    long count = productRepository.countByIdIn(productIds);
    if (count != productIds.size()) {
        throw new ValidationException("Một số sản phẩm không tồn tại trong hệ thống");
    }

    // Xóa tất cả sản phẩm theo danh sách ID
    productRepository.deleteAllById(productIds);
}
}
