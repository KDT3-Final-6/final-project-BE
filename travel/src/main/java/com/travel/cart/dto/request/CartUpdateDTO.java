package com.travel.cart.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CartUpdateDTO {

    @NotNull
    private Long periodOptionId;

    @NotNull
    private Integer quantity;
}
