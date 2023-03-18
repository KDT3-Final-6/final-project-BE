package com.travel.admin.controller;

import com.travel.product.dto.PeriodPostRequestDTO;
import com.travel.product.dto.ProductPostRequestDTO;
import com.travel.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admins")
public class AdminController {

    private final ProductService productService;

    @PostMapping("/products")
    public ResponseEntity<String> postProduct(@RequestBody @Valid ProductPostRequestDTO productPostRequestDTO) {
        productService.createProduct(productPostRequestDTO);
        return ResponseEntity.ok(null);
    }

    @PostMapping("/products/periods")
    public ResponseEntity postPeriod(@RequestBody @Valid PeriodPostRequestDTO periodPostRequestDTO) {
        productService.createPeriodOptions(periodPostRequestDTO);
        return ResponseEntity.ok(null);
    }
}
