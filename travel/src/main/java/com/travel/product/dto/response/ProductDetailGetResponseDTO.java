package com.travel.product.dto.response;

import com.travel.image.entity.ProductImage;
import com.travel.product.entity.Product;
import com.travel.product.entity.ProductCategory;
import com.travel.product.entity.Status;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Getter
public class ProductDetailGetResponseDTO {

    private String productName;
    private String productThumbnail;
    private List<String> productImages;
    private Integer productPrice;
    private String productStatus;
    private String productContent;
    private String contentDetail;

    private List<PeriodOptionsInProduct> periodOptions = new ArrayList<>();
    private List<CategoriesInProduct> productCategories = new ArrayList<>();

    public ProductDetailGetResponseDTO(Product product) {
        this.productName = product.getProductName();
        this.productThumbnail = product.getProductImages().get(0).getImagePath();
        this.productImages = product.getProductImages().stream()
                .map(ProductImage::getImagePath).collect(toList()).subList(1, product.getProductImages().size());
        this.productPrice = product.getProductPrice();
        this.productStatus = product.getProductStatus().getKorean();
        this.productContent = product.getProductContent();
        this.contentDetail = product.getContentDetail();
        this.periodOptions.addAll(product.getPeriodOptions().stream()
                .filter(periodOption -> !periodOption.getPeriodOptionStatus().equals(Status.HIDDEN))
                .map(PeriodOptionsInProduct::new)
                .collect(toList()));

        List<Long> categoryIds = new ArrayList<>();
        for (ProductCategory productCategory : product.getProductCategories()) {
            categoryIds.add(productCategory.getCategory().getCategoryId());
        }
        for (ProductCategory productCategory : product.getProductCategories()) {
            if (productCategory.getCategory().getParent() == null) {
                productCategories.add(CategoriesInProduct.of(productCategory.getCategory(), categoryIds));
            }
        }
    }
}
