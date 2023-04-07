package com.travel.product.service;

import com.travel.global.response.PageResponseDTO;
import com.travel.image.entity.Image;
import com.travel.image.entity.ProductImage;
import com.travel.image.repository.ProductImageRepository;
import com.travel.image.service.FileUploadService;
import com.travel.member.entity.Member;
import com.travel.member.exception.MemberException;
import com.travel.member.exception.MemberExceptionType;
import com.travel.member.repository.MemberRepository;
import com.travel.product.dto.request.PeriodPostRequestDTO;
import com.travel.product.dto.request.ProductPatchRequestDTO;
import com.travel.product.dto.request.ProductPostRequestDTO;
import com.travel.product.dto.response.CategoryListGetResponseDTO;
import com.travel.product.dto.response.ProductDetailGetResponseDTO;
import com.travel.product.dto.response.ProductListGetResponseDTO;
import com.travel.product.entity.*;
import com.travel.product.exception.ProductException;
import com.travel.product.exception.ProductExceptionType;
import com.travel.product.repository.CategoryRepository;
import com.travel.product.repository.PeriodOptionRepository;
import com.travel.product.repository.ProductCategoryRepository;
import com.travel.product.repository.product.ProductRepository;
import com.travel.wishlist.repository.WishlistRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
    private final ProductImageRepository productImageRepository;
    private final MemberRepository memberRepository;
    private final WishlistRepository wishlistRepository;
    private final FileUploadService fileUploadService;


    @Transactional
    public void createProduct(ProductPostRequestDTO productPostRequestDTO, MultipartFile thumbnailFile, List<MultipartFile> imageFiles) throws IOException {
        Image thumbnail = fileUploadService.upload(thumbnailFile);
        List<Image> images = fileUploadService.uploads(imageFiles);
        images.add(0, thumbnail);
        Product product = productPostRequestDTO.toEntity();
        List<ProductImage> productImages = product.addImages(images);
        productRepository.save(product);
        productImageRepository.saveAll(productImages);
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


    public PageResponseDTO displayProductsByAdmin(String userEmail, Pageable pageable) {

        Page<Product> products = productRepository.findAllByOrderByProductIdDesc(pageable);

        Page<ProductListGetResponseDTO> page = productsToDTO(products, pageable, userEmail);

        return new PageResponseDTO(page);
    }

    public PageResponseDTO displayProductsByMember(String userEmail, Pageable pageable, Boolean includeSoldOut) {

        Page<Product> products = productRepository.findAllWithCheckBox(pageable, includeSoldOut);

        Page<ProductListGetResponseDTO> page = productsToDTO(products, pageable, userEmail);

        return new PageResponseDTO(page);
    }

    public ProductDetailGetResponseDTO displayProductDetail(Long id, String memberEmail) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductException(ProductExceptionType.PRODUCT_NOT_FOUND));

        return new ProductDetailGetResponseDTO(product, isExistsByMemberAndProduct(memberEmail, product));
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
        if (productPatchRequestDTO.getCategoryIds() != null) {
            List<Category> categories = categoryRepository.findAllById(productPatchRequestDTO.getCategoryIds());
            productCategoryRepository.deleteAllByProduct(product);
            List<ProductCategory> productCategories = IntStream.range(0, categories.size())
                    .mapToObj(i -> new ProductCategory(product, categories.get(i)))
                    .collect(toList());
            productCategoryRepository.saveAll(productCategories);
        }
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

    @Transactional
    public void deletePeriodOption(Long id) {
        PeriodOption periodOption = periodOptionRepository.findById(id)
                .orElseThrow(() -> new ProductException(ProductExceptionType.PERIOD_OPTION_NOT_FOUND));

        periodOption.setPeriodOptionStatus(Status.HIDDEN);
    }

    private boolean isExistsByMemberAndProduct(String memberEmail, Product product) {
        boolean existsByMemberAndProduct = false;
        if (memberEmail != null) {
            Member member = memberRepository.findByMemberEmail(memberEmail)
                    .orElseThrow(() -> new MemberException(MemberExceptionType.MEMBER_NOT_FOUND));
            existsByMemberAndProduct = wishlistRepository.existsByMemberAndProduct(member, product);
        }
        return existsByMemberAndProduct;
    }

    private Page<ProductListGetResponseDTO> productsToDTO(Page<Product> products, Pageable pageable, String userEmail) {

        return new PageImpl<>(
                products.getContent().stream()
                        .map(product -> new ProductListGetResponseDTO(product, isExistsByMemberAndProduct(userEmail, product)))
                        .collect(toList()), pageable, products.getTotalElements());
    }
}