package com.travel.search.service;

import com.travel.global.exception.GlobalException;
import com.travel.global.exception.GlobalExceptionType;
import com.travel.global.response.PageResponseDTO;
import com.travel.product.dto.response.ProductCategoryToProductPage;
import com.travel.product.entity.*;
import com.travel.product.exception.ProductException;
import com.travel.product.exception.ProductExceptionType;
import com.travel.product.repository.CategoryRepository;
import com.travel.product.repository.PeriodOptionRepository;
import com.travel.product.repository.ProductCategoryRepository;
import com.travel.product.repository.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SearchService {

    private final CategoryRepository categoryRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final ProductRepository productRepository;
    private final PeriodOptionRepository periodOptionRepository;

    public ProductCategoryToProductPage displayProductsByCategory(Pageable pageable, String categoryName) {

        Category category = categoryRepository.findByCategoryName(categoryName)
                .orElseThrow(() -> new ProductException(ProductExceptionType.CATEGORY_NOT_FOUND));

        return new ProductCategoryToProductPage(productCategoryRepository.findAllByCategory(pageable, category));
    }

    public PageResponseDTO searchProducts(
            Pageable pageable,
            String title,
            String categoryName,
            String startDate,
            String endDate) {

        List<Product> products = productRepository.findByProductStatusNot(Status.HIDDEN);

        List<Product> categoryProducts = categoryNameNotNull(categoryName);

        List<Product> nameContainingProducts = titleNotNull(title);

        List<Product> periodOptionProducts = dateNotNull(startDate, endDate);

        List<Product> productList = products.stream()
                .filter(product -> categoryProducts == null || categoryProducts.contains(product))
                .filter(product -> nameContainingProducts == null || nameContainingProducts.contains(product))
                .filter(product -> periodOptionProducts == null || periodOptionProducts.contains(product))
                .filter(product -> product.getProductStatus() == Status.FORSALE)
                .collect(Collectors.toList());

        return new PageResponseDTO(new PageImpl<>(productList, pageable, productList.size()));
    }

    private List<Product> titleNotNull(String title) {
        List<Product> nameContainingProducts = null;
        if (title != null) {
            nameContainingProducts = productRepository.findByProductNameContaining(title);
        }

        return nameContainingProducts;
    }

    private List<Product> dateNotNull(String startDate, String endDate) {
        List<Product> periodOptionProducts = null;
        if (startDate != null && endDate != null) {
            LocalDate startLocalDate = validateDate(startDate);
            LocalDate endLocalDate = validateDate(endDate);

            periodOptionProducts = periodOptionRepository.findByStartDateAndEndDate(startLocalDate, endLocalDate).stream()
                    .filter(periodOption -> periodOption.getPeriodOptionStatus() == Status.FORSALE)
                    .map(PeriodOption::getProduct)
                    .collect(Collectors.toList());
        }

        return periodOptionProducts;
    }

    private List<Product> categoryNameNotNull(String categoryName) {
        List<Product> categoryProducts = null;

        Category category = categoryRepository.findByCategoryName(categoryName)
                .orElseThrow(() -> new ProductException(ProductExceptionType.CATEGORY_NOT_FOUND));

        categoryProducts = productCategoryRepository.findAllByCategory(category).stream()
                .map(ProductCategory::getProduct)
                .collect(Collectors.toList());

        return categoryProducts;
    }

    private LocalDate validateDate(String date) {
        String pattern = "^\\d{4}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])$";
        if (!date.matches(pattern)) {
            throw new GlobalException(GlobalExceptionType.WRONG_DATE_FORMAT);
        }

        return LocalDate.parse(date);
    }
}