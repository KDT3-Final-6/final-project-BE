package com.travel.order.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Setter
@Getter
public class OrderCreateDTO {

    @NotNull
    private Long periodOptionId;

    @NotNull
    private Integer quantity;
}
