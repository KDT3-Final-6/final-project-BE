package com.travel.cart.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class CartDeleteListDTO {

    @NotNull
    @Valid List<CartDeleteDTO> cartIds;
}
