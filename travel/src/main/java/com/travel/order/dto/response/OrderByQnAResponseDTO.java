package com.travel.order.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class OrderByQnAResponseDTO {

    private Long orderId;

    private Long purchasedProductId;

    private String productName;

    @Builder
    public OrderByQnAResponseDTO(Long orderId, Long purchasedProductId, String productName) {
        this.orderId = orderId;
        this.purchasedProductId = purchasedProductId;
        this.productName = productName;
    }
}
