package com.travel.order.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
public class OrderListAdminResponseDTO {

    private LocalDate orderDate;

    private List<OrderAdminResponseDTO> orderList;

    @Builder
    public OrderListAdminResponseDTO(List<OrderAdminResponseDTO> orderList) {
        this.orderDate = LocalDate.from(orderList.get(0).getOrderDate());
        this.orderList = orderList;
    }
}
