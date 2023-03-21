package com.travel.admin.controller;

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
    public ResponseEntity<PageResponseDTO> getProducts(@RequestParam(required = false, defaultValue = "1") String page) {

        PageRequest pageRequest = null;

        try {
            int intPage = Integer.parseInt(page);
            pageRequest = PageRequest.of(intPage - 1, PAGE_SIZE);
            //정상적인 범위 내의 페이지 번호면 해당 페이지로
        } catch (IllegalArgumentException e) {
            pageRequest = PageRequest.of(0, PAGE_SIZE);
            //음수나 오버플로 발생시키는 페이지 번호면 0번페이지로
        }

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
