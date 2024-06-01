package com.project.productservice.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OptionDTO {
    @JsonProperty("options")
    private String option;
    @JsonProperty("prices")
    private String price;
    @JsonProperty("product_id")
    private Long productId;
    private Long id;

}
