package com.travel.post.dto.request;

import lombok.Getter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
public class ReviewUpdateRequestDTO {

    @NotEmpty
    private String content;

    @NotNull
    @Min(1)
    @Max(5)
    private int scope;
}
