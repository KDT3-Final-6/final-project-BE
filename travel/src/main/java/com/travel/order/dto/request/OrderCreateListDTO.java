package com.travel.order.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Setter
@Getter
public class OrderCreateListDTO {

    @NotNull
    @Valid
    private List<OrderCreateDTO> productIds;

    @NotEmpty
    private String paymentMethod;
}
