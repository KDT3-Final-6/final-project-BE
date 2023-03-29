package com.travel.search.controller;

import com.travel.global.exception.GlobalException;
import com.travel.global.exception.GlobalExceptionType;
import com.travel.global.response.PageResponseDTO;
import com.travel.search.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
    public ResponseEntity<PageResponseDTO> getProductsByCategory(
            @RequestParam String category,
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false) String sortTarget,
            Authentication authentication) {

        if (page < 1) {
            throw new GlobalException(GlobalExceptionType.PAGE_INDEX_NOT_POSITIVE_NUMBER);
        }
        PageRequest pageRequest = PageRequest.of(page - 1, PAGE_SIZE);

        PageResponseDTO pageResponseDTO;
        if (authentication == null) {
            // 로그인되지 않은 사용자용 정보를 반환
            pageResponseDTO = searchService.displayProductsByCategory(pageRequest, category, sortTarget, null);
        } else {
            // 로그인된 사용자용 정보를 반환
            String memberEmail = authentication.getName();
            pageResponseDTO = searchService.displayProductsByCategory(pageRequest, category, sortTarget, memberEmail);
        }

        return ResponseEntity.ok(pageResponseDTO);
    }

    @GetMapping
    public ResponseEntity<PageResponseDTO> searchProducts(
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String sortTarget,
            Authentication authentication
    ) {

        if (page < 1) {
            throw new GlobalException(GlobalExceptionType.PAGE_INDEX_NOT_POSITIVE_NUMBER);
        }
        PageRequest pageRequest = PageRequest.of(page - 1, PAGE_SIZE);

        PageResponseDTO pageResponseDTO;
        if (authentication == null) {
            // 로그인되지 않은 사용자용 정보를 반환
            pageResponseDTO = searchService.searchProducts(pageRequest, keyword, sortTarget, null);
        } else {
            // 로그인된 사용자용 정보를 반환
            String memberEmail = authentication.getName();
            pageResponseDTO = searchService.searchProducts(pageRequest, keyword, sortTarget, memberEmail);
        }

        return ResponseEntity.ok(pageResponseDTO);
    }
}
