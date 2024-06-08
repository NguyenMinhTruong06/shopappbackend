package com.project.productservice.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderDetailDTO {
    @JsonProperty("order_id")
    @Min(value = 1,message = "User's ID must be >0")
    private Long orderId;
    @JsonProperty("product_id")
    @Min(value = 1,message = "productId ID must be >0")
    private Long productId;
    @Min(value = 0,message = "price must be >0")
    private Float price;


    @JsonProperty("number_of_products")
    @Min(value = 1,message = "number_of_product must be >0")
    private int numberOfProduct;

    @JsonProperty("total_money")
    @Min(value = 0,message = "total money must be >0")
    private Float totalMoney;
    @JsonProperty("product_option")
    private String option;
    @JsonProperty("product_name")
    private String productName;

}

