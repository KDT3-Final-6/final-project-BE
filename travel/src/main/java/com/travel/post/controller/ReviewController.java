package com.travel.post.controller;

import com.travel.post.dto.request.ReviewCreateRequestDTO;
import com.travel.post.dto.request.ReviewUpdateRequestDTO;
import com.travel.post.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<Void> createReview(
            @Valid @RequestBody ReviewCreateRequestDTO reviewCreateRequestDTO,
            Authentication authentication
    ) {

        reviewService.createReview(reviewCreateRequestDTO, authentication.getName());

        return ResponseEntity.ok(null);
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
