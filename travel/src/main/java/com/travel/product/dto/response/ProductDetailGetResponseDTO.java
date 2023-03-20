package com.travel.product.dto.response;

import com.travel.product.entity.PeriodOption;
import com.travel.product.entity.Product;
import com.travel.product.entity.ProductCategory;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ProductDetailGetResponseDTO {

    private String productName;
    private String productThumbnail;
    private Integer productPrice;
    private String productStatus;
    private String productContent;
    private String contentDetail;

    private List<PeriodOption> periodOptions = new ArrayList<>();
    private List<String> productCategories = new ArrayList<>();

    public ProductDetailGetResponseDTO(Product product) {
        this.productName = product.getProductName();
        this.productThumbnail = product.getProductThumbnail();
        this.productPrice = product.getProductPrice();
        this.productStatus = product.getProductStatus().getKorean();
        this.productContent = product.getProductContent();
        this.contentDetail = product.getContentDetail();
        this.periodOptions.addAll(product.getPeriodOptions());
        for (ProductCategory productCategory : product.getProductCategories()) {
            this.productCategories.add(productCategory.getCategory().getCategoryEnum().getKorean());
        }
    }
}
