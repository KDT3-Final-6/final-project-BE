package com.travel.product.dto.response;

import com.travel.product.entity.Category;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class CategoryListGetResponseDTO {
    private Long categoryId;
    private String categoryName;
    private List<CategoryListGetResponseDTO> children = new ArrayList<>();

    public static CategoryListGetResponseDTO of(Category category) {
        return new CategoryListGetResponseDTO(
                category.getCategoryId(),
                category.getCategoryName(),
                category.getChildren().stream().map(CategoryListGetResponseDTO::of).collect(Collectors.toList())
        );
    }


    public CategoryListGetResponseDTO(Long categoryId, String categoryName, List<CategoryListGetResponseDTO> children) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.children = children;
    }
}
