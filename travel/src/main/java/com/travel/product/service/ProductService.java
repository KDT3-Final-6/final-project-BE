package com.travel.product.service;

import com.travel.product.dto.request.PeriodPostRequestDTO;
import com.travel.product.dto.request.ProductPostRequestDTO;
import com.travel.product.entity.Category;
import com.travel.product.entity.PeriodOption;
import com.travel.product.entity.Product;
import com.travel.product.entity.ProductCategory;
import com.travel.product.exception.ProductException;
import com.travel.product.exception.ProductExceptionType;
import com.travel.product.repository.CategoryRepository;
import com.travel.product.repository.PeriodOptionRepository;
import com.travel.product.repository.ProductCategoryRepository;
import com.travel.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;
    private final PeriodOptionRepository periodOptionRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final CategoryRepository categoryRepository;


    @Transactional
    public void createProduct(ProductPostRequestDTO productPostRequestDTO) {
        Product product = productRepository.save(productPostRequestDTO.toEntity());
        List<Category> categories = categoryRepository.findAllById(productPostRequestDTO.getCategoryIds());

        /*
        스트림으로 변환 전 코드

        List<ProductCategory> productCategories = new ArrayList<>();
        for (int i = 0; i < productPostRequestDTO.getCategoryIds().size(); i++) {
            productCategories.add(new ProductCategory(product, categories.get(i)));
        }
         */

        List<ProductCategory> productCategories = IntStream.range(0, categories.size())
                .mapToObj(i -> new ProductCategory(product, categories.get(i)))
                .collect(toList());

        productCategoryRepository.saveAll(productCategories);
    }

    @Transactional
    public void createPeriodOptions(PeriodPostRequestDTO periodPostRequestDTO) {

//        Product product = productRepository.findById(periodPostRequestDTO.getProductId())
//                .orElseThrow(()->new ProductException(ProductExceptionType.PRODUCT_NOT_FOUND));

        Product product = productRepository.findById(periodPostRequestDTO.getProductId())
                .orElseThrow(() -> {
                    ProductExceptionType exceptionType = ProductExceptionType.PRODUCT_NOT_FOUND;
                    log.error(exceptionType.getErrorMsg());
                    return new ProductException(exceptionType);
                });

        List<PeriodOption> periodOptionList = periodPostRequestDTO.toEntities();

        /*
        for (PeriodOption periodOption : periodOptionList) {
            product.addPeriodOption(periodOption);
        }
        */

        periodOptionList.forEach(product::addPeriodOption);
    }
}