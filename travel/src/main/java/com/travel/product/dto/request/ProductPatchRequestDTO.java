package com.travel.product.dto.request;

import com.travel.product.entity.Product;
import com.travel.product.entity.Status;
import lombok.Getter;

import javax.validation.constraints.Positive;
import java.util.Optional;

@Getter
public class ProductPatchRequestDTO {

    private String productName;
    private String productStatus;
    private String productContent;
    private String contentDetail;

    @Positive
    private Integer productPrice;

    public Status setEnumProductStatus(String productStatus) {
        if (productStatus.equals(Status.FORSALE.getKorean())) {
            return Status.FORSALE;
        } else if (productStatus.equals(Status.SOLDOUT.getKorean())) {
            return Status.SOLDOUT;
        }
        return Status.HIDDEN;
    }

    public Product toEntity(Product product) {
        productName = Optional.ofNullable(productName).orElse(product.getProductName());
        productStatus = Optional.ofNullable(productStatus).orElse(product.getProductStatus().getKorean());
        productContent = Optional.ofNullable(productContent).orElse(product.getProductContent());
        contentDetail = Optional.ofNullable(contentDetail).orElse(product.getContentDetail());
        productPrice=Optional.ofNullable(productPrice).orElse(product.getProductPrice());

        return Product.builder()
                .productName(productName)
                .productPrice(productPrice)
                .productStatus(setEnumProductStatus(productStatus))
                .productContent(productContent)
                .contentDetail(contentDetail)
                .build();
    }
}