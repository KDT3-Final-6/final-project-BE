package com.travel.order.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@AllArgsConstructor
public class OrderAdminResponseDTO extends OrderResponseDTO {

    private Long memberId;

    private String memberName;

    private String memberEmail;

    private String paymentMethod;
}
