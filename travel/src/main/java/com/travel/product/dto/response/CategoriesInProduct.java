package com.travel.product.dto.response;

import com.travel.product.entity.Category;
import lombok.Getter;

import java.util.List;

@Getter
public class CategoriesInProduct {
    private String categoryName;
    private CategoriesInProduct child;

    public static CategoriesInProduct of(Category category, List<Long> categoryIds) {
        return new CategoriesInProduct(
                category.getCategoryName(),
                category.getChild()
                        .stream()
                        .filter(child -> categoryIds.contains(child.getCategoryId()))
                        .map(child -> CategoriesInProduct.of(child, categoryIds))
                        .findFirst().orElse(null));
    }

    public CategoriesInProduct(String categoryName, CategoriesInProduct child) {
        this.categoryName = categoryName;
        this.child = child;
    }
}
