package com.travel.product.controller;

import com.travel.global.response.PageResponseDTO;
import com.travel.product.dto.response.ProductListGetResponseDTO;
import com.travel.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.travel.admin.controller.AdminController.PAGE_SIZE;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

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
}
