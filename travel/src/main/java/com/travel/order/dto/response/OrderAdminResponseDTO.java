package com.travel.order.dto.response;

import com.travel.order.entity.OrderStatus;
import com.travel.order.entity.PaymentMethod;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class OrderAdminResponseDTO {

    private Long orderId;

    private Long productId;

    private Long purchasedProductId;

    private Long memberId;

    private String memberName;

    private String memberEmail;

    private String productName;

    private String productThumbnail;

    private Integer productPrice;

    private LocalDateTime orderDate;

    private String periodOptionName;

    private Integer purchasedProductQuantity;

    private String orderStatus;

    private String paymentMethod;

    @Builder
    public OrderAdminResponseDTO(
            Long orderId, Long productId, Long purchasedProductId, Long memberId, String memberName, String memberEmail,
            String productName, String productThumbnail, Integer productPrice, LocalDateTime orderDate,
            String periodOptionName, Integer purchasedProductQuantity, OrderStatus orderStatus, PaymentMethod paymentMethod
    ) {
        this.orderId = orderId;
        this.productId = productId;
        this.purchasedProductId = purchasedProductId;
        this.memberId = memberId;
        this.memberName = memberName;
        this.memberEmail = memberEmail;
        this.productName = productName;
        this.productThumbnail = productThumbnail;
        this.productPrice = productPrice;
        this.orderDate = orderDate;
        this.periodOptionName = periodOptionName;
        this.purchasedProductQuantity = purchasedProductQuantity;
        this.orderStatus = orderStatus.getKorean();
        this.paymentMethod = paymentMethod.getKorean();
    }
}
