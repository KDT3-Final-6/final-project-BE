package com.travel.product.service;

import com.travel.global.response.PageResponseDTO;
import com.travel.image.entity.Image;
import com.travel.image.repository.ImageRepository;
import com.travel.image.service.FileUploadService;
import com.travel.product.dto.request.PeriodPostRequestDTO;
import com.travel.product.dto.request.ProductPatchRequestDTO;
import com.travel.product.dto.request.ProductPostRequestDTO;
import com.travel.product.dto.response.CategoryListGetResponseDTO;
import com.travel.product.dto.response.ProductDetailGetResponseDTO;
import com.travel.product.dto.response.ProductListGetResponseDTO;
import com.travel.product.entity.Category;
import com.travel.product.entity.PeriodOption;
import com.travel.product.entity.Product;
import com.travel.product.entity.ProductCategory;
import com.travel.product.exception.ProductException;
import com.travel.product.exception.ProductExceptionType;
import com.travel.product.repository.CategoryRepository;
import com.travel.product.repository.PeriodOptionRepository;
import com.travel.product.repository.ProductCategoryRepository;
import com.travel.product.repository.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
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
    private final ImageRepository imageRepository;
    private final FileUploadService fileUploadService;


    @Transactional
    public void createProduct(ProductPostRequestDTO productPostRequestDTO, MultipartFile thumbnailFile, List<MultipartFile> imageFiles) throws IOException {
        Image thumbnail = fileUploadService.upload(thumbnailFile);
        List<Image> images = fileUploadService.uploads(imageFiles);
        images.add(0, thumbnail);
        Product product = productPostRequestDTO.toEntity();
        product.addImages(images);
        productRepository.save(product);
        imageRepository.saveAll(images);
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

        Product product = productRepository.findById(periodPostRequestDTO.getProductId())
                .orElseThrow(() -> new ProductException(ProductExceptionType.PRODUCT_NOT_FOUND));

        List<PeriodOption> periodOptionList = periodPostRequestDTO.toEntities();

        /*
        for (PeriodOption periodOption : periodOptionList) {
            product.addPeriodOption(periodOption);
        }
        */

        periodOptionList.forEach(product::addPeriodOption);
    }


    public PageResponseDTO displayProductsByAdmin(Pageable pageable) {

        return new PageResponseDTO(productRepository.findAll(pageable)
                .map(ProductListGetResponseDTO::new));
    }

    public PageResponseDTO displayProductsByMember(Pageable pageable, Boolean includeSoldOut) {

        return new PageResponseDTO(productRepository.findAllWithCheckBox(pageable, includeSoldOut)
                .map(ProductListGetResponseDTO::new));
    }

    public ProductDetailGetResponseDTO displayProductDetail(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductException(ProductExceptionType.PRODUCT_NOT_FOUND));

        return new ProductDetailGetResponseDTO(product);
    }

    @Transactional
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductException(ProductExceptionType.PRODUCT_NOT_FOUND));

        product.changeStatusToHidden(product);
    }

    @Transactional
    public void updateProduct(Long id, ProductPatchRequestDTO productPatchRequestDTO) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductException(ProductExceptionType.PRODUCT_NOT_FOUND));

        product.updateProduct(productPatchRequestDTO.toEntity(product));
    }

    public List<CategoryListGetResponseDTO> displayCategories() {

        List<CategoryListGetResponseDTO> result = new ArrayList<>();
        List<Category> categories = categoryRepository.findAll();
        for (Category c : categories) {
            if (c.getParent() == null)
                result.add(CategoryListGetResponseDTO.of(c));
        }

        return result;
    }
}