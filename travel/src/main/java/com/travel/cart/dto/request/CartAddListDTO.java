package com.travel.cart.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Setter
@Getter
public class CartAddListDTO {

    @NotNull
    @Valid
    private List<CartAddDTO> productIds;
}
