package com.travel.post.dto.request;

import lombok.Getter;

import javax.validation.constraints.NotEmpty;

@Getter
public class QnARequestDTO {

    @NotEmpty
    private String title;

    @NotEmpty
    private String content;

    @NotEmpty
    private String inquiryType;

    private Long purchasedProductId;
}
