package com.project.productservice.responses;


import com.project.productservice.models.OrderDetail;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Data
@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailResponse {
    private Long id;
    @JoinColumn(name = "order_id")
    private Long orderId;


    @JoinColumn(name = "product_id")
    private Long productId;

    private Float price;

    private int numberOfProducts;

    private Float totalMoney;
    private String option;
    private String productName;

    public static OrderDetailResponse fromOrderDetail(OrderDetail orderDetail){
        return OrderDetailResponse.builder()
                .id(orderDetail.getId())
                .orderId(orderDetail.getOrder().getId())
                .productName(orderDetail.getProductName())
                .productId(orderDetail.getProductId())
                .price(orderDetail.getPrice())
                .numberOfProducts(orderDetail.getNumberOfProducts())
                .totalMoney(orderDetail.getTotalMoney())
                .option(orderDetail.getOption())
                .build();
    }
}
