package com.travel.search.service.impl;

import com.travel.global.response.PageResponseDTO;
import com.travel.member.entity.Member;
import com.travel.member.exception.MemberException;
import com.travel.member.exception.MemberExceptionType;
import com.travel.member.repository.MemberRepository;
import com.travel.product.entity.Category;
import com.travel.product.entity.Product;
import com.travel.product.entity.ProductCategory;
import com.travel.product.entity.Status;
import com.travel.product.repository.CategoryRepository;
import com.travel.product.repository.ProductCategoryRepository;
import com.travel.product.repository.product.ProductRepository;
import com.travel.search.dto.request.SortTarget;
import com.travel.search.dto.response.SearchResultResponseDTO;
import com.travel.search.service.SearchService;
import com.travel.wishlist.repository.WishlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SearchServiceImpl implements SearchService {

    private final CategoryRepository categoryRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;
    private final WishlistRepository wishlistRepository;

    @Override
    public PageResponseDTO displayProductsByCategory(Pageable pageable, String categoryName, String sortTarget, String memberEmail) {
        List<Product> productList = findCategoryNameProducts(categoryName).stream()
                .filter(product -> product.getProductStatus() == Status.FORSALE)
                .collect(Collectors.toList());

        if (sortTarget.equals("인기순")) {
            productList = productList.stream()
                    .sorted(Comparator.comparing(Product::getWishlistCount).reversed())
                    .collect(Collectors.toList());
        }

        List<SearchResultResponseDTO> searchResult = productList.stream()
                .map(product -> product.toSearchResultResponseDTO(isExistsByMemberAndProduct(memberEmail, product)))
                .collect(Collectors.toList());

        return new PageResponseDTO(new PageImpl<>(searchResult));
    }

    @Override
    public PageResponseDTO searchProducts(
            Pageable pageable,
            String keyword,
            String sortTarget,
            String memberEmail
    ) {

        List<Product> categoryProducts = findCategoryNameProducts(keyword);

        List<Product> nameContainingProducts = findTitleProducts(keyword);

        List<Product> productList = Stream.of(categoryProducts, nameContainingProducts)
                .flatMap(Collection::stream)
                .distinct()
                .filter(product -> product.getProductStatus() == Status.FORSALE)
                .collect(Collectors.toList());

        if (sortTarget != null) {
            productList = sortList(productList, sortTarget);
        }

        List<SearchResultResponseDTO> searchResult = productList.stream()
                .map(product -> product.toSearchResultResponseDTO(isExistsByMemberAndProduct(memberEmail, product)))
                .collect(Collectors.toList());

        return new PageResponseDTO(new PageImpl<>(searchResult, pageable, searchResult.size()));
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

    private List<Product> findTitleProducts(String keyword) {
        List<Product> productNameContaining = productRepository.findByProductNameContaining(keyword);

        if (productNameContaining == null) {
            return Collections.emptyList();
        }

        return productNameContaining;
    }

    private List<Product> findCategoryNameProducts(String keyword) {
        Category category = categoryRepository.findByCategoryName(keyword)
                .orElse(null);

        if (category == null) {
            return Collections.emptyList();
        }

        return productCategoryRepository.findAllByCategory(category).stream()
                .map(ProductCategory::getProduct)
                .collect(Collectors.toList());
    }

    private List<Product> sortList(List<Product> products, String sortTarget) {
        SortTarget target = Stream.of(SortTarget.values())
                .filter(sort -> sortTarget.equals(sort.getKorean()))
                .findFirst()
                .orElse(null);

        if (target == null) {
            return products;
        }

        if (target.equals(SortTarget.BY_PRICE_DESC)) {
            return products.stream()
                    .sorted(Comparator.comparing(Product::getProductPrice).reversed())
                    .collect(Collectors.toList());
        } else if (target.equals(SortTarget.BY_PRICE_ASC)) {
            return products.stream()
                    .sorted(Comparator.comparing(Product::getProductPrice))
                    .collect(Collectors.toList());
        } else if (target.equals(SortTarget.BY_POPULARITY)) { // 아직 구현 안됨
            return products.stream()
                    .sorted(Comparator.comparing(Product::getWishlistCount).reversed())
                    .collect(Collectors.toList());
        }

        return products;
    }
}