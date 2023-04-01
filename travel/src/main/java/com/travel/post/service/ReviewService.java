package com.travel.post.service;

import com.travel.post.dto.request.ReviewCreateRequestDTO;

public interface ReviewService {

    void createReview(ReviewCreateRequestDTO reviewCreateRequestDTO, String memberEmail);
}
