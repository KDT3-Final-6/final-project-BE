package com.travel.order.dto.request;

import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Getter
public class OrderNonMemberCreateDTO extends OrderCreateListDTO {

    @NotEmpty
    private String memberName;

    @NotEmpty
    @Email
    private String memberEmail;
}
