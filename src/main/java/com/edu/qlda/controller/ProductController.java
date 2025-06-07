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

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.beans.PropertyEditorSupport;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/products")
@CrossOrigin("http://localhost:4200")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<Product> getAllProducts() {
        return productService.listProducts();
    }
    @GetMapping("/{productId}")
    public Product getProductById(@PathVariable int productId) {
        return productService.getProductById(productId);
    }

    @GetMapping("/by-ids/{ids}")
    public ResponseEntity<List<Product>> getProductsByIds(@PathVariable String ids) {
        try {
            List<Integer> productIds = Arrays.stream(ids.split(",")).map(Integer::parseInt).toList();
            return ResponseEntity.ok(productService.findProductsByIds(productIds));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Integer.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                try {
                    setValue((text == null || text.trim().isEmpty() || "null".equalsIgnoreCase(text)) ? null : Integer.parseInt(text));
                } catch (NumberFormatException e) {
                    setValue(null);
                }
            }
        });
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Messageresponse<Product>> addProduct(@Valid @ModelAttribute ProductDto productDto,
                                                               BindingResult bindingResult) throws IOException {
        if (bindingResult.hasErrors()) {
            String message = bindingResult.getFieldErrors().stream()
                    .map(error -> List.of("quantity", "price").contains(error.getField()) ? "Giá trị nhập vào sai định dạng" : error.getDefaultMessage())
                    .collect(Collectors.joining(", "));
            return ResponseEntity.ok(new Messageresponse<>(400, message));
        }
        String filename = storeFile(productDto.getAvatarImage());
        Product product = productService.createProduct(productDto, filename);
        return ResponseEntity.ok(new Messageresponse<>(200, "Thêm mới sản phẩm thành công", product));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Messageresponse<Product>> updateProduct(@ModelAttribute ProductDto productDto,
                                                                  BindingResult bindingResult,
                                                                  @PathVariable Integer id) throws IOException {
        String filename = null;
        if (productDto.getAvatarImage() != null && !productDto.getAvatarImage().isEmpty()) {
            filename = storeFile(productDto.getAvatarImage());
        }
        Product product = productService.updateProduct(productDto, id, filename);
        return ResponseEntity.ok(new Messageresponse<>(200, "Cập nhật sản phẩm thành công", product));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Messageresponse<Product>> deleteProduct(@PathVariable Integer id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(new Messageresponse<>(200, "Xóa sản phẩm thành công"));
    }

    @PostMapping("/bulk-delete")
    public ResponseEntity<Messageresponse<List<Integer>>> deleteProducts(@RequestBody List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Messageresponse<>(400, "Danh sách sản phẩm cần xóa không được để trống"));
        }
        List<Integer> notFoundIds = productService.deleteProducts(ids);
        if (notFoundIds.isEmpty()) {
            return ResponseEntity.ok(new Messageresponse<>(200, "Đã xóa tất cả sản phẩm thành công"));
        } else {
            return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                    .body(new Messageresponse<>(206, "Một số sản phẩm không tồn tại", notFoundIds));
        }
    }

    @GetMapping("/images/{imagename}")
    public ResponseEntity<Object> getProductImage(@PathVariable String imagename) {
        try {
            Path imagePath = Paths.get("uploads", imagename);
            Resource resource = new UrlResource(imagePath.toUri());
            if (!resource.exists()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Image not found: " + imagename);
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(resource);

        } catch (MalformedURLException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid image URL: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while fetching image: " + e.getMessage());
        }
    }

    private String storeFile(MultipartFile file) throws IOException {
        if (file == null || file.getOriginalFilename() == null) {
            throw new IllegalArgumentException("File hoặc tên file không được null hoặc rỗng");
        }
        List<String> allowedTypes = List.of("image/jpeg", "image/png", "image/jpg", "image/gif");
        if (!allowedTypes.contains(file.getContentType())) {
            throw new IllegalArgumentException("File không đúng định dạng! Chỉ chấp nhận JPG, PNG, GIF.");
        }
        String filename = UUID.randomUUID() + "." + StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        Path filepath = Paths.get("uploads");
        if (!Files.exists(filepath)) Files.createDirectories(filepath);
        Files.copy(file.getInputStream(), filepath.resolve(filename), StandardCopyOption.REPLACE_EXISTING);
        return filename;
    }
}
