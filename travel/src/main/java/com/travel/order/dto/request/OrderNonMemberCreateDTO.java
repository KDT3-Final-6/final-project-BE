package com.travel.order.dto.request;

import lombok.Getter;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
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
    @Pattern(regexp = "^01([0|1|6|7|8|9])-?([0-9]{3,4})-?([0-9]{4})$", message = "-없이 작성해주세요")
    private String memberPhone;

    private String memberEmail;
}
