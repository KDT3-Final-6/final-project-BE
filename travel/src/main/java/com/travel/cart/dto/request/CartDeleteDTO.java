package com.travel.cart.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CartDeleteDTO {

    @NotNull
    private Long cartId;
}
