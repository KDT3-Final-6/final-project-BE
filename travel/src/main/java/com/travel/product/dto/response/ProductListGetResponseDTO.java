package com.travel.product.dto.response;

import com.travel.product.entity.Product;
import lombok.Getter;

@Getter
public class ProductListGetResponseDTO {

    private Long productId;
    private String productName;
    private String productThumbnail;
    private Integer productPrice;
    private String productStatus;
    private String productContent;
    private String contentDetail;

    public ProductListGetResponseDTO(Product product) {
        this.productId = product.getProductId();
        this.productName = product.getProductName();
        this.productThumbnail = product.getProductThumbnail();
        this.productPrice = product.getProductPrice();
        this.productStatus = product.getProductStatus().getKorean();
        this.productContent = product.getProductContent();
        this.contentDetail = product.getContentDetail();
    }
}
