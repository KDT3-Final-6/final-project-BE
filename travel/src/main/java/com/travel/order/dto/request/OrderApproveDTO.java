package com.travel.order.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Setter
@Getter
public class OrderApproveDTO {

    @NotNull
    private Long memberId;
}
