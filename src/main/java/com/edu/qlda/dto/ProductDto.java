package com.edu.qlda.dto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    String name;
    Integer price;
    String thumbnail;
    Integer quantity;
   @JsonProperty("category_id")
    Integer categoryid;
    private MultipartFile avatarImage;

}
