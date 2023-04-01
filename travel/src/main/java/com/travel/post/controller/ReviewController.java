package com.travel.post.controller;

import com.travel.post.dto.request.ReviewCreateRequestDTO;
import com.travel.post.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
