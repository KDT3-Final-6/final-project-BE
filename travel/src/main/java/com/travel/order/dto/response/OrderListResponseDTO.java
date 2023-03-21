package com.travel.order.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
public class OrderListResponseDTO {

    private LocalDate orderDate;

    private List<OrderResponseDTO> orderList;

    @Builder
    public OrderListResponseDTO(List<OrderResponseDTO> orderList) {
        this.orderDate = LocalDate.from(orderList.get(0).getOrderDate());
        this.orderList = orderList;
    }
}
