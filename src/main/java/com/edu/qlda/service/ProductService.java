package com.edu.qlda.service;
import com.edu.qlda.dto.ProductDto;

import com.edu.qlda.entity.Category;
import com.edu.qlda.entity.Product;
import com.edu.qlda.entity.ProductImage;
import com.edu.qlda.repository.CategoryRepository;
import com.edu.qlda.repository.ProductImageRepository;
import com.edu.qlda.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductImageRepository productImageRepository;
    public  ProductService(ProductRepository productRepository, CategoryRepository categoryRepository, ProductImageRepository productImageRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.productImageRepository = productImageRepository;
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
       product.setPrice(productDto.getPrice());
       product.setThumbnail(filename);
       product.setQuantity(productDto.getQuantity());
return productRepository.save(product);
}
    public void saveproductImage (Integer  productId,String filename){
      Product exitproduct = productRepository.findByIdProduct(productId);
         ProductImage productImage = new ProductImage();
         productImage.setProduct(exitproduct);
         productImage.setImageurl(filename);
         productImageRepository.save(productImage);
    }

    public void deleteproduct(Integer id){
        productRepository.deleteById(id);
    }
}
