package com.edu.qlda.controller;
import com.edu.qlda.dto.ProductDto;



import com.edu.qlda.entity.Product;
import com.edu.qlda.playload.response.Messageresponse;
import com.edu.qlda.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;

import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.beans.PropertyEditorSupport;
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

    @InitBinder
    protected void initBinder(WebDataBinder  binder) {
        // Xử lý dữ liệu đầu vào cho Integer: Chuyển "null" hoặc chuỗi rỗng thành null
        binder.registerCustomEditor(Integer.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                if (text == null || text.trim().equalsIgnoreCase("null") || text.trim().isEmpty()) {
                    setValue(null);
                } else {
                    try {
                        setValue(Integer.parseInt(text));
                    } catch (NumberFormatException e) {
                        setValue(null); // Nếu không phải số hợp lệ, đặt giá trị null
                    }
                }
            }
        });
    }
    // thêm mới 1 sản phấm
    @PostMapping(value = "/addproduct", consumes = "multipart/form-data")
    public ResponseEntity<Messageresponse<Product>> addProduct(@Valid @ModelAttribute ProductDto productDto,
                                                               BindingResult bindingResult) throws IOException {
        String filename = storeFile(productDto.getAvatarImage());


            FieldError fieldError = bindingResult.getFieldError();


            if (bindingResult.hasErrors()) {
                String message = "";
                // Xử lý lỗi ép kiểu
                if ((fieldError.getField().equals("quantity") || fieldError.getField().equals("price"))
                        && fieldError.getDefaultMessage().contains("không được để trống")) {
                    message = "Giá trị nhập vào sai định dạng";
                } else {
                    message = (fieldError != null) ? fieldError.getDefaultMessage() : "";
                }
                Messageresponse<Product> response = new Messageresponse<>(201, message);
                return ResponseEntity.ok(response);
            
        }
        Product product = productService.createproduct(productDto, filename);
        Messageresponse<Product> response = new Messageresponse<>(200,"Thêm mới sản phẩm thành công", product);
        return ResponseEntity.ok(response);
    }
    @PutMapping(value = "/updateproduct/{id}",consumes = "multipart/form-data")
    public ResponseEntity<Messageresponse<Product>> updateProduct(@ModelAttribute ProductDto productDto, BindingResult bindingResult, @PathVariable Integer id) throws IOException {
         String filename = storeFile(productDto.getAvatarImage());
         Product product = productService.updateproduct(productDto, id, filename);
         Messageresponse<Product> response = new Messageresponse<>(202,"Cập nhật sản phẩm thành công",product);
        return ResponseEntity.ok(response);
    }
    private String storeFile(MultipartFile file) throws IOException {
        if (file == null || file.getOriginalFilename() == null ) {
            throw new IllegalArgumentException("File or filename must not be null or empty");
        }
        // Lấy định dạng file
        String fileType = file.getContentType();
        // Danh sách định dạng ảnh hợp lệ
        List<String> allowedTypes = List.of("image/jpeg", "image/png", "image/jpg", "image/gif");
        // Kiểm tra nếu định dạng file không hợp lệ
        if (!allowedTypes.contains(fileType)) {
            throw new IllegalArgumentException("File không đúng định dạng! Chỉ chấp nhận JPG, PNG, GIF.");
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