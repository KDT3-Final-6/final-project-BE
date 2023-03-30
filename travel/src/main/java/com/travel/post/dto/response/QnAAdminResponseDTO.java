package com.travel.post.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class QnAAdminResponseDTO {

    private Long postId;

    private String postTitle;

    private String postContent;

    private String inquiryType;

    private String qnAStatus;

    private String purchasedProductName;

    private LocalDateTime createdDate;

    private String memberName;
}
