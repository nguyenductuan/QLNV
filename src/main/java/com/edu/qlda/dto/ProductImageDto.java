package com.edu.qlda.dto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductImageDto {
    @JsonProperty("product_id")
    Integer productid;
    @JsonProperty("image_url")
 String imageurl;

}
