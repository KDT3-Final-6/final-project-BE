package com.travel.post.dto.response;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@SuperBuilder
public class QnAResponseDTO {

    private Long postId;

    private String postTitle;

    private String postContent;

    private String inquiryType;

    private String qnAStatus;

    private String answer;

    private LocalDateTime replyDate;

    private String purchasedProductName;

    private LocalDateTime createdDate;
}
