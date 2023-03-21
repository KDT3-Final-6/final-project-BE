package com.travel.admin.controller;

import com.travel.global.exception.GlobalException;
import com.travel.global.exception.GlobalExceptionType;
import com.travel.global.response.PageResponseDTO;
import com.travel.product.dto.request.PeriodPostRequestDTO;
import com.travel.product.dto.request.ProductPostRequestDTO;
import com.travel.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admins")
public class AdminController {

    public static final int PAGE_SIZE = 3;

    private final ProductService productService;

    @PostMapping("/products")
    public ResponseEntity<String> postProduct(@RequestBody @Valid ProductPostRequestDTO productPostRequestDTO) {
        productService.createProduct(productPostRequestDTO);
        return ResponseEntity.ok(null);
    }

    @GetMapping("/products")
    public ResponseEntity<PageResponseDTO> getProducts(@RequestParam(required = false, defaultValue = "1") int page) {

        if (page < 1) {
            throw new GlobalException(GlobalExceptionType.PAGE_INDEX_NOT_POSITIVE_NUMBER);
        }

        PageRequest pageRequest = PageRequest.of(page - 1, PAGE_SIZE);

        return ResponseEntity.ok(productService.displayProductsByAdmin(pageRequest));
    }

    @PostMapping("/products/periods")
    public ResponseEntity<String> postPeriod(@RequestBody @Valid PeriodPostRequestDTO periodPostRequestDTO) {
        productService.createPeriodOptions(periodPostRequestDTO);
        return ResponseEntity.ok(null);
    }

    /**
     *
     * @param productId
     * delete지만 실제 db에서는 삭제 하지 않는다
     * 그냥 Member가 못보게 Status만 숨김으로 변경
     * @return void
     */
    @DeleteMapping("/products/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.ok(null);
    }
}
