package com.travel.post.controller;

import com.travel.global.exception.GlobalException;
import com.travel.global.exception.GlobalExceptionType;
import com.travel.global.response.PageResponseDTO;
import com.travel.post.dto.request.ReviewCreateRequestDTO;
import com.travel.post.dto.request.ReviewUpdateRequestDTO;
import com.travel.post.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

    public static final int PAGE_SIZE = 10;

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<Void> createReview(
            @Valid @RequestBody ReviewCreateRequestDTO reviewCreateRequestDTO,
            Authentication authentication
    ) {

        reviewService.createReview(reviewCreateRequestDTO, authentication.getName());

        return ResponseEntity.ok(null);
    }

    @GetMapping("/posts")
    public ResponseEntity<PageResponseDTO> getReviews(
            @RequestParam(required = false, defaultValue = "1") int page
    ) {

        if (page < 1) {
            throw new GlobalException(GlobalExceptionType.PAGE_INDEX_NOT_POSITIVE_NUMBER);
        }

        PageRequest pageRequest = PageRequest.of(page - 1, PAGE_SIZE);

        PageResponseDTO pageResponseDTO = reviewService.getReviews(pageRequest);

        return ResponseEntity.ok(pageResponseDTO);
    }

    @GetMapping
    public ResponseEntity<PageResponseDTO> getReviewsByMember(
            @RequestParam(required = false, defaultValue = "1") int page,
            Authentication authentication
    ) {

        if (page < 1) {
            throw new GlobalException(GlobalExceptionType.PAGE_INDEX_NOT_POSITIVE_NUMBER);
        }

        PageRequest pageRequest = PageRequest.of(page - 1, PAGE_SIZE);

        PageResponseDTO pageResponseDTO = reviewService.getReviewsByMember(pageRequest, authentication.getName());

        return ResponseEntity.ok(pageResponseDTO);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<PageResponseDTO> getReviewsByProduct(
            @RequestParam(required = false, defaultValue = "1") int page,
            @PathVariable Long productId
    ) {
        if (page < 1) {
            throw new GlobalException(GlobalExceptionType.PAGE_INDEX_NOT_POSITIVE_NUMBER);
        }

        PageRequest pageRequest = PageRequest.of(page - 1, PAGE_SIZE);

        PageResponseDTO pageResponseDTO = reviewService.getReviewsByProduct(pageRequest, productId);

        return ResponseEntity.ok(pageResponseDTO);
    }

    @PatchMapping("/{postId}")
    public ResponseEntity<Void> updateReview(
            @PathVariable Long postId,
            @Valid @RequestBody ReviewUpdateRequestDTO reviewUpdateRequestDTO,
            Authentication authentication
    ) {

        reviewService.updateReview(postId, reviewUpdateRequestDTO, authentication.getName());

        return ResponseEntity.ok(null);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long postId, Authentication authentication) {
        reviewService.deleteReview(postId, authentication.getName());

        return ResponseEntity.ok(null);
    }
}
