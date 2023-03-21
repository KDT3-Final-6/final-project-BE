package com.travel.product.dto.response;

import com.travel.product.entity.Category;
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
    private List<StringBuffer> productCategories = new ArrayList<>();

    public ProductDetailGetResponseDTO(Product product) {
        this.productName = product.getProductName();
        this.productThumbnail = product.getProductThumbnail();
        this.productPrice = product.getProductPrice();
        this.productStatus = product.getProductStatus().getKorean();
        this.productContent = product.getProductContent();
        this.contentDetail = product.getContentDetail();
        this.periodOptions.addAll(product.getPeriodOptions());

        for (ProductCategory productCategory : product.getProductCategories()) {
            if (productCategory.getCategory().getChild().size() == 0) {
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append(productCategory.getCategory().getCategoryEnum().getKorean());
                Category parent = productCategory.getCategory().getParent();
                while (parent != null) {
                    stringBuffer.insert(0, ": ").insert(0, parent.getCategoryEnum().getKorean());
                    parent = parent.getParent();
                }
                productCategories.add(stringBuffer);
            }
        }
    }
}
