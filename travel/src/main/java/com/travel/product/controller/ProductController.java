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
    public ResponseEntity<PageResponseDTO> getProducts(@RequestParam(required = false, defaultValue = "1") String page,
                                                       @RequestParam(required = false, defaultValue = "false") Boolean includeSoldOut) {
        PageRequest pageRequest = null;

        try {
            int intPage = Integer.parseInt(page);
            pageRequest = PageRequest.of(intPage - 1, PAGE_SIZE);
            //정상적인 범위 내의 페이지 번호면 해당 페이지로
        } catch (IllegalArgumentException e) {
            pageRequest = PageRequest.of(0, PAGE_SIZE);
            //음수나 오버플로 발생시키는 페이지 번호면 0번페이지로
        }
        return ResponseEntity.ok(productService.displayProductsByMember(pageRequest, includeSoldOut));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductDetailGetResponseDTO> getProductDetail(@PathVariable Long productId) {
        return ResponseEntity.ok(productService.displayProductDetail(productId));
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
}
