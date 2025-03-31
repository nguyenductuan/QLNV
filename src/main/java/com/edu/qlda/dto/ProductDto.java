package com.edu.qlda.dto;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    @NotBlank(message = "Tên không được để trống")
    String name;
    @NotNull(message = "Giá không được để trống")
    @Min(value = 0, message = "Giá phải là số nguyên dương")
    Integer price;
    String thumbnail;
    @NotNull(message = "Trạng thái không được để trống")

    Integer status;
    @Min(value = 0, message = "Giá phải là số nguyên dương")
    @NotNull(message = "Số lượng không được để trống")
    Integer quantity;
    @NotNull(message = "Nhóm sản phẩm không được để trống")
    @JsonProperty("category_id")
    Integer categoryid;
    @NotNull(message = "Ảnh không được để trống")
    private MultipartFile avatarImage;

}
