package com.travel.product.dto;

import com.travel.product.entity.Product;
import com.travel.product.entity.Status;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.util.ArrayList;
import java.util.List;

@Getter
public class ProductPostRequestDTO {

    @NotBlank
    private String productName;

    @NotBlank
    private String productThumbnail;

    @Positive
    private Integer productPrice;

    @NotBlank
    private String productStatus;

    @NotBlank
    private String productContent;

    @NotBlank
    private String contentDetail;

    private List<Long> CategoryIds = new ArrayList<>();

    public Status setEnumProductStatus(String productStatus) {
        if (productStatus.equals(Status.FORSALE.getKorean())) {
            return Status.FORSALE;
        } else if (productStatus.equals(Status.SOLDOUT.getKorean())) {
            return Status.SOLDOUT;
        }
        return Status.HIDDEN;
    }

    public Product toEntity() {
        return Product.builder()
                .productName(productName)
                .productThumbnail(productThumbnail)
                .productPrice(productPrice)
                .productStatus(setEnumProductStatus(productStatus))
                .productContent(productContent)
                .contentDetail(contentDetail)
                .build();
    }


}
