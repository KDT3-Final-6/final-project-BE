package com.travel.post.dto.response;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Getter
@SuperBuilder
public class ReviewInMemberDTO {

    private Long postId;

    private Long productId;

    private String purchasedProductName;

    private String purchasedProductThumbnail;

    private String postContent;

    private int scope;

    private LocalDate modifiedDate;
}
