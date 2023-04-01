package com.travel.post.service;

import com.travel.global.response.PageResponseDTO;
import com.travel.post.dto.request.ReviewCreateRequestDTO;
import com.travel.post.dto.request.ReviewUpdateRequestDTO;
import org.springframework.data.domain.Pageable;

public interface ReviewService {

    void createReview(ReviewCreateRequestDTO reviewCreateRequestDTO, String memberEmail);

    PageResponseDTO getReviewsByMember(Pageable pageable, String memberEmail);

    PageResponseDTO getReviewsByProduct(Pageable pageable, Long productId);

    void updateReview(Long postId, ReviewUpdateRequestDTO reviewUpdateRequestDTO, String memberEmail);

    void deleteReview(Long postId, String memberEmail);
}
