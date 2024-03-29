package com.travel.cart.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CartResponseDTO {

    private Long cartId;

    private Long productId;

    private Long periodOptionId;

    private Integer productPrice;

    private String productName;

    private String periodOptionName;

    private String productThumbnail;

    private String productContent;

    private Integer cartQuantity;

    private String productStatus;

    private String periodOptionStatus;
}
