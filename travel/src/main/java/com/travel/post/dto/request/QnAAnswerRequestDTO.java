package com.travel.post.dto.request;

import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
public class QnAAnswerRequestDTO {

    @NotNull
    private Long postId;

    @NotEmpty
    private String content;
}
