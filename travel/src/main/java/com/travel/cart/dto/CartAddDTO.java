package com.travel.cart.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CartAddDTO {

    @NotNull
    private Long productId;

    @NotNull
    private Long periodOptionId;

    @NotNull
    private Integer quantity;
}
