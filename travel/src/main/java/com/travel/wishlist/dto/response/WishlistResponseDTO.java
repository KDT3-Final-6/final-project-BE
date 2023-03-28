package com.travel.wishlist.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class WishlistResponseDTO {

    private Long wishilistId;

    private Long productId;

    private String productName;

    private String productThumbnail;

    private Integer productPrice;
}
