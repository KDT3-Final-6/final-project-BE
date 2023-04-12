package com.travel.order.dto.request;

import lombok.Getter;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
public class OrderCreateListDTO {

    @NotNull
    @Valid
    private List<OrderCreateDTO> productIds;

    @NotEmpty
    private String paymentMethod;
}
