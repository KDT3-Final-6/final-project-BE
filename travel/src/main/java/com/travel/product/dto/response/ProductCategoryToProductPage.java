package com.travel.product.dto.response;

import com.travel.product.entity.ProductCategory;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Getter
public class ProductCategoryToProductPage {

    private List<ProductListGetResponseDTO> content = new ArrayList<>();
    private int totalPages;
    private long totalElements;
    private int pageNumber;
    private int size;

    public ProductCategoryToProductPage(Page<ProductCategory> page) {
        this.content.addAll(page.getContent().stream()
                .map(ProductCategory::getProduct)
                .map(ProductListGetResponseDTO::new)
                .collect(toList()));
        this.totalPages = page.getTotalPages();
        this.totalElements = page.getTotalElements();
        this.pageNumber = page.getNumber() + 1;
        this.size = page.getSize();
    }
}