package com.travel.post.service;

import com.travel.post.dto.request.ReviewCreateRequestDTO;
import com.travel.post.dto.request.ReviewUpdateRequestDTO;

public interface ReviewService {

    void createReview(ReviewCreateRequestDTO reviewCreateRequestDTO, String memberEmail);

    void updateReview(Long postId, ReviewUpdateRequestDTO reviewUpdateRequestDTO, String memberEmail);
}
