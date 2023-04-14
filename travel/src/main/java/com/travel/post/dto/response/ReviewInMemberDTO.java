package com.travel.post.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class ReviewInMemberDTO {

    private Long postId;

    private Long productId;

    private String purchasedProductName;

    private String purchasedProductThumbnail;

    private String postContent;

    private int scope;

    private LocalDate modifiedDate;
}
