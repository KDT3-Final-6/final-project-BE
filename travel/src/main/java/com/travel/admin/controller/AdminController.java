package com.travel.admin.controller;

import com.travel.global.exception.GlobalException;
import com.travel.global.exception.GlobalExceptionType;
import com.travel.global.response.PageResponseDTO;
import com.travel.order.dto.request.OrderApproveDTO;
import com.travel.order.service.OrderService;
import com.travel.post.service.QnAService;
import com.travel.product.dto.request.PeriodPostRequestDTO;
import com.travel.product.dto.request.ProductPatchRequestDTO;
import com.travel.product.dto.request.ProductPostRequestDTO;
import com.travel.product.dto.response.CategoryListGetResponseDTO;
import com.travel.product.dto.response.ProductDetailGetResponseDTO;
import com.travel.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admins")
public class AdminController {

    public static final int PAGE_SIZE = 3;

    private final ProductService productService;
    private final OrderService orderService;
    private final QnAService qnAService;

    @PostMapping("/products")
    public ResponseEntity<String> postProduct(@RequestPart @Valid ProductPostRequestDTO productPostRequestDTO,
                                              @RequestPart("thumbnail") MultipartFile thumbnail,
                                              @RequestPart("images") List<MultipartFile> images) throws IOException {
        productService.createProduct(productPostRequestDTO, thumbnail, images);
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

    @GetMapping("/products/{productId}")
    public ResponseEntity<ProductDetailGetResponseDTO> getProductDetail(@PathVariable Long productId) {
        return ResponseEntity.ok(productService.displayProductDetail(productId));
    }

    @PostMapping("/products/periods")
    public ResponseEntity<String> postPeriod(@RequestBody @Valid PeriodPostRequestDTO periodPostRequestDTO) {
        productService.createPeriodOptions(periodPostRequestDTO);
        return ResponseEntity.ok(null);
    }

    @DeleteMapping("/products/periods/{periodOptionId}")
    public ResponseEntity<String> deletePeriod(@PathVariable Long periodOptionId) {
        productService.deletePeriodOption(periodOptionId);
        return ResponseEntity.ok(null);
    }

    @DeleteMapping("/products/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.ok(null);
    }

    @PatchMapping("/products/{productId}")
    public ResponseEntity<String> patchProduct(@PathVariable Long productId,
                                               @RequestBody ProductPatchRequestDTO productPatchRequestDTO) {
        productService.updateProduct(productId, productPatchRequestDTO);
        return ResponseEntity.ok(null);
    }


    @GetMapping("/categories")
    public ResponseEntity<List<CategoryListGetResponseDTO>> getCategories() {
        return ResponseEntity.ok(productService.displayCategories());
    }

    @GetMapping("/orders")
    public ResponseEntity<PageResponseDTO> getOrders(@RequestParam(required = false, defaultValue = "1") int page, Authentication authentication) {
        if (page < 1) {
            throw new GlobalException(GlobalExceptionType.PAGE_INDEX_NOT_POSITIVE_NUMBER);
        }

        PageRequest pageRequest = PageRequest.of(page - 1, PAGE_SIZE);

        PageResponseDTO orders = orderService.getOrdersAdmin(pageRequest, authentication.getName());

        return ResponseEntity.ok(orders);
    }

    @PatchMapping("/orders/approvals/{orderId}")
    public ResponseEntity<Void> approveOrder(@PathVariable Long orderId, @RequestBody OrderApproveDTO orderApproveDTO, Authentication authentication) {
        orderService.approveOrder(orderId, orderApproveDTO, authentication.getName());

        return ResponseEntity.ok(null);
    }

    @GetMapping("/qna")
    public ResponseEntity<PageResponseDTO> getQnAs(@RequestParam(required = false, defaultValue = "1") int page, Authentication authentication) {
        if (page < 1) {
            throw new GlobalException(GlobalExceptionType.PAGE_INDEX_NOT_POSITIVE_NUMBER);
        }

        PageRequest pageRequest = PageRequest.of(page - 1, PAGE_SIZE);

        PageResponseDTO pageResponseDTO = qnAService.getQnAsAdmin(pageRequest, authentication.getName());

        return ResponseEntity.ok(pageResponseDTO);
    }
}
