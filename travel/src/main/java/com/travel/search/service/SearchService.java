package com.travel.search.service;

import com.travel.product.dto.response.ProductCategoryToProductPage;
import com.travel.product.entity.Category;
import com.travel.product.entity.CategoryEnum;
import com.travel.product.exception.ProductException;
import com.travel.product.exception.ProductExceptionType;
import com.travel.product.repository.CategoryRepository;
import com.travel.product.repository.ProductCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SearchService {

    private final CategoryRepository categoryRepository;
    private final ProductCategoryRepository productCategoryRepository;


    public ProductCategoryToProductPage displayProductsByCategory(Pageable pageable, String categoryKorean) {
        CategoryEnum categoryEnum = Stream.of(CategoryEnum.values())
                .filter(c -> categoryKorean.equals(c.getKorean()))
                .findFirst()
                .orElse(null);

        Category category = categoryRepository.findByCategoryEnum(categoryEnum)
                .orElseThrow(() -> new ProductException(ProductExceptionType.CATEGORY_NOT_FOUND));

        return new ProductCategoryToProductPage(productCategoryRepository.findAllByCategory(pageable, category));
    }
}