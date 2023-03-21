package com.travel.search.controller;

import com.travel.global.exception.GlobalException;
import com.travel.global.exception.GlobalExceptionType;
import com.travel.product.dto.response.ProductCategoryToProductPage;
import com.travel.search.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.travel.admin.controller.AdminController.PAGE_SIZE;

@RestController
@RequiredArgsConstructor
@RequestMapping("/search")
public class SearchController {

    private final SearchService searchService;

    @GetMapping("/category")
    public ResponseEntity<ProductCategoryToProductPage> getProductsByCategory(
            @RequestParam String category, @RequestParam(required = false, defaultValue = "1") int page) {

        if (page < 1) {
            throw new GlobalException(GlobalExceptionType.PAGE_INDEX_NOT_POSITIVE_NUMBER);
        }
        PageRequest pageRequest = PageRequest.of(page - 1, PAGE_SIZE);

        return ResponseEntity.ok(searchService.displayProductsByCategory(pageRequest, category));
    }
}
