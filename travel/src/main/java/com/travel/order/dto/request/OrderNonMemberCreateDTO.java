package com.travel.order.dto.request;

import lombok.Getter;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
public class OrderNonMemberCreateDTO {

    @NotNull
    @Valid
    private List<OrderCreateDTO> productIds;

    @NotEmpty
    private String paymentMethod;

    @NotEmpty
    private String memberName;

    @NotEmpty
    @Email
    private String memberEmail;
}
