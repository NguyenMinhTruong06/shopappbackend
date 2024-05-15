package com.project.productservice.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDTO {
    private String name;

    @Min(value = 0, message = "price must be greater than or equal to 0")
    @Max(value = 10000000,message = "Price must be less than or equal to 10000000")
    private  Float price;
    @Min(value = 0, message = "Discount must be greater than or equal to 0")
    @Max(value = 100, message = "Discount must be less than or equal to 100")
    private int discount;

    private String thumbnail;
    private String description;

    @JsonProperty("category_id")
    private Long categoryId;


}
