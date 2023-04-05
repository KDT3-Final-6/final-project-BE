package com.travel.product.controller;

import com.travel.global.exception.GlobalException;
import com.travel.global.exception.GlobalExceptionType;
import com.travel.global.response.PageResponseDTO;
import com.travel.product.dto.response.ProductDetailGetResponseDTO;
import com.travel.product.service.ProductService;
import com.travel.search.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import static com.travel.admin.controller.AdminController.PAGE_SIZE;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final SearchService searchService;

    @GetMapping
    public ResponseEntity<PageResponseDTO> getProducts(@RequestParam(required = false, defaultValue = "1") int page,
                                                       @RequestParam(required = false, defaultValue = "false") Boolean includeSoldOut,
                                                       Authentication authentication) {
        if (page < 1) {
            throw new GlobalException(GlobalExceptionType.PAGE_INDEX_NOT_POSITIVE_NUMBER);
        }

        PageRequest pageRequest = PageRequest.of(page - 1, PAGE_SIZE);
        PageResponseDTO pageResponseDTO = null;

        if (authentication == null) {
            // 로그인되지 않은 사용자용 정보를 반환
            pageResponseDTO = productService.displayProductsByMember(null, pageRequest, includeSoldOut);
        } else {
            // 로그인된 사용자용 정보를 반환
            String memberEmail = authentication.getName();
            pageResponseDTO = productService.displayProductsByMember(memberEmail, pageRequest, includeSoldOut);
        }

        return ResponseEntity.ok(pageResponseDTO);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductDetailGetResponseDTO> getProductDetail(@PathVariable Long productId,
                                                                        Authentication authentication) {

        ProductDetailGetResponseDTO productDetail = null;

        if (authentication == null) {
            // 로그인되지 않은 사용자용 정보를 반환
            productDetail = productService.displayProductDetail(productId, null);
        } else {
            // 로그인된 사용자용 정보를 반환
            String memberEmail = authentication.getName();
            productDetail = productService.displayProductDetail(productId, memberEmail);
        }

        return ResponseEntity.ok(productDetail);
    }

    @GetMapping("/recommend")
    public ResponseEntity<PageResponseDTO> getRecommend(
            @RequestParam(required = false, defaultValue = "1") int page,
            Authentication authentication
    ) {

        if (page < 1) {
            throw new GlobalException(GlobalExceptionType.PAGE_INDEX_NOT_POSITIVE_NUMBER);
        }
        PageRequest pageRequest = PageRequest.of(page - 1, PAGE_SIZE);

        PageResponseDTO pageResponseDTO = searchService.getRecommend(pageRequest, authentication.getName());

        return ResponseEntity.ok(pageResponseDTO);
    }

    @GetMapping("/relation/{productId}")
    public ResponseEntity<PageResponseDTO> getRelatedProducts(
            @RequestParam(required = false, defaultValue = "1") int page,
            @PathVariable Long productId,
            Authentication authentication
    ) {

        if (page < 1) {
            throw new GlobalException(GlobalExceptionType.PAGE_INDEX_NOT_POSITIVE_NUMBER);
        }
        PageRequest pageRequest = PageRequest.of(page - 1, PAGE_SIZE);

        PageResponseDTO pageResponseDTO;
        if (authentication == null) {
            // 로그인되지 않은 사용자용 정보를 반환
            pageResponseDTO = searchService.getRelatedProducts(pageRequest, productId, null);
        } else {
            // 로그인된 사용자용 정보를 반환
            pageResponseDTO = searchService.getRelatedProducts(pageRequest, productId, authentication.getName());
        }

        return ResponseEntity.ok(pageResponseDTO);
    }
}
