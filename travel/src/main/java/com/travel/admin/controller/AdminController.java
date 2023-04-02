package com.travel.admin.controller;

import com.travel.admin.dto.responseDTO.MemberListDTO;
import com.travel.admin.service.AdminService;
import com.travel.auth.jwt.JwtTokenProvider;
import com.travel.global.exception.GlobalException;
import com.travel.global.exception.GlobalExceptionType;
import com.travel.global.response.PageResponseDTO;
import com.travel.order.dto.request.OrderApproveDTO;
import com.travel.order.service.OrderService;
import com.travel.post.dto.request.QnAAnswerRequestDTO;
import com.travel.post.service.QnAService;
import com.travel.product.dto.request.PeriodPostRequestDTO;
import com.travel.product.dto.request.ProductPatchRequestDTO;
import com.travel.product.dto.request.ProductPostRequestDTO;
import com.travel.product.dto.response.CategoryListGetResponseDTO;
import com.travel.product.dto.response.ProductDetailGetResponseDTO;
import com.travel.product.service.ProductService;
import com.travel.search.service.SearchService;
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
    private final SearchService searchService;
    private final AdminService adminService;
    private final JwtTokenProvider tokenProvider;

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

    @PostMapping("/qna/answers")
    public ResponseEntity<Void> createAnswer(@Valid @RequestBody QnAAnswerRequestDTO qnAAnswerRequestDTO, Authentication authentication) {
        qnAService.createAnswer(qnAAnswerRequestDTO, authentication.getName());

        return ResponseEntity.ok(null);
    }

    @PatchMapping("/qna/answers")
    public ResponseEntity<Void> updateAnswer(@Valid @RequestBody QnAAnswerRequestDTO qnAAnswerRequestDTO) {
        qnAService.updateAnswer(qnAAnswerRequestDTO);

        return ResponseEntity.ok(null);
    }

    @GetMapping("/qna/search")
    public ResponseEntity<PageResponseDTO> searchQnAs(
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false) String qnAStatus,
            @RequestParam(required = false) String inquiryType,
            @RequestParam(required = false) String keyword
    ) {
        if (page < 1) {
            throw new GlobalException(GlobalExceptionType.PAGE_INDEX_NOT_POSITIVE_NUMBER);
        }

        PageRequest pageRequest = PageRequest.of(page - 1, PAGE_SIZE);

        PageResponseDTO pageResponseDTO = searchService.searchQnAs(pageRequest, qnAStatus, inquiryType, keyword);

        return ResponseEntity.ok(pageResponseDTO);
    }

    @GetMapping("/members")
    public ResponseEntity<PageResponseDTO>getAllMember(@RequestParam(required = false, defaultValue = "1") int page) {
        if (page < 1) {
            throw new GlobalException(GlobalExceptionType.PAGE_INDEX_NOT_POSITIVE_NUMBER);
        }

        PageRequest pageRequest = PageRequest.of(page - 1, PAGE_SIZE);

        PageResponseDTO pageResponseDTO = adminService.getAllMembers(pageRequest);

        return ResponseEntity.ok(pageResponseDTO);
    }

    @PostMapping("/upMember/{memberId}")
    public ResponseEntity<?> changeMemberToAdmin(@PathVariable Long memberId) {
        adminService.changeMemberToAdmin(memberId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/downMember/{memberId}")
    public ResponseEntity<?> changeAdminToUser(@PathVariable Long memberId) {
        adminService.changeAdminToMember(memberId);
        return ResponseEntity.ok().build();
    }
}
