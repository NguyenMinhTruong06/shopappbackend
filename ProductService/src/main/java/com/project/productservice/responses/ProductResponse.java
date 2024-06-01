package com.project.productservice.responses;

import com.fasterxml.jackson.annotation.JsonProperty;


import com.project.productservice.models.Option;
import com.project.productservice.models.Product;

import com.project.productservice.models.ProductImage;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ProductResponse extends BaseResponse {
    private Long id;
    private String name;


//    private  Float price;

    private int discount;

    private String thumbnail;
    private String description;

    @JsonProperty("category_id")
    private Long categoryId;

    private List<ProductImage> images;
    private List<Option> options;
//    private List<Float> prices;


    public static ProductResponse fromProduct(Product product){
        ProductResponse productResponse =
                ProductResponse.builder()
                        .id(product.getId())
                        .name(product.getName())
//                        .price(product.getPrice())
                        .thumbnail(product.getThumbnail())
                        .description(product.getDescription())
                        .categoryId(product.getCategory().getId())
                        .images(product.getImages())
                        .options(product.getOptions())
                        .build();

        productResponse.setCreatedAt(product.getCreatedAt());
        productResponse.setUpdatedAt(product.getUpdatedAt());

        return productResponse;
    }
}
