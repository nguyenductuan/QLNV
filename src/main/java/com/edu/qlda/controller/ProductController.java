package com.edu.qlda.controller;
import com.edu.qlda.dto.ProductDto;

import com.edu.qlda.entity.Product;
import com.edu.qlda.playload.response.Messageresponse;
import com.edu.qlda.service.ProductService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

@RestController
@CrossOrigin("http://localhost:4200")
public class ProductController {
    private final ProductService productService;
    public ProductController(ProductService productService) {
        this.productService = productService;
    }
    @GetMapping("/product")
    public List<Product> getAllProducts() {
        return productService.listproducts();
    }
    // Xem chi tiết sản phẩm
    @GetMapping("/productid")
    public Product productById(int productId) {
        return productService.productById(productId);
    }
    @GetMapping("/productIds")
    public ResponseEntity<List<Product>> productByIds(@RequestParam("ids") String ids) {
        try {
            List<Integer> productIds = Arrays.stream(ids.split(",")).map(Integer::parseInt).toList();
            List<Product> products = productService.findProductsByIds(productIds);
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    // thêm mới 1 sản phấm
    @PostMapping(value = "/addproduct", consumes = "multipart/form-data")
    public ResponseEntity<Messageresponse<Product>> addProduct(@ModelAttribute ProductDto productDto) throws IOException {
        String filename = storeFile(productDto.getAvatarImage());
        Product product = productService.createproduct(productDto, filename);
        Messageresponse<Product> response = new Messageresponse<>(200,"Thêm mới sản phẩm thành công", product);
        return ResponseEntity.ok(response);
    }
    @PutMapping(value = "/updateproduct/{id}",consumes = "multipart/form-data")
    public ResponseEntity<Messageresponse<Product>> updateProduct( @ModelAttribute ProductDto productDto, @PathVariable Integer id) throws IOException {
         String filename = storeFile(productDto.getAvatarImage());
         Product product = productService.updateproduct(productDto, id, filename);
         Messageresponse<Product> response = new Messageresponse<>(202,"Cập nhật sản phẩm thành công",product);
        return ResponseEntity.ok(response);
    }
    private String storeFile(MultipartFile file) throws IOException {
        if (file == null || file.getOriginalFilename() == null ) {
            throw new IllegalArgumentException("File or filename must not be null or empty");
        }
        // Đảm bảo filename không phải null trước khi truyền vào cleanPath
        String filename = StringUtils.cleanPath(Optional.ofNullable(file.getOriginalFilename()).orElse(""));
        //Thêm UUID để đảm bảo file duy nhất
        String uniqdnamefile = UUID.randomUUID().toString() + "." + filename;
        // Đường dẫn đến thư mục muốn lưu file
        Path filepath = Paths.get("uploads");
        //Kểm tra và tạo thư mục nếu nó không tồn tại
        if (!Files.exists(filepath)) {
            Files.createDirectories(filepath);
        }
        //Đường dẫn đầy đủ đến file
        Path discus = Paths.get(filepath.toString(), uniqdnamefile);
        //Sao chép file vào thư mục đích
        Files.copy(file.getInputStream(), discus, StandardCopyOption.REPLACE_EXISTING);
        return uniqdnamefile;
    }
    //Xóa sản phẩm
    @DeleteMapping("/delete-product/{id}")
    public ResponseEntity<Messageresponse<Product>> deleteProduct(@PathVariable("id") Integer productId) {
        productService.deleteproduct(productId);
        Messageresponse<Product> response = new Messageresponse<>(200,"Xóa sản phẩm thành công");
        return ResponseEntity.ok(response);
    }
    //Hàm xem thông tin ảnh
    @GetMapping("/product/images/{imagename}")
    public ResponseEntity<Resource> getProductImages(@PathVariable("imagename") String imagename) {
        try {
            Path imagePath = Paths.get("uploads/" + imagename);
            UrlResource urlResource = new UrlResource(imagePath.toUri());
            if (urlResource.exists()) {
                return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(urlResource);

            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (MalformedURLException e) {
            return ResponseEntity.notFound().build();
        }
    }
}