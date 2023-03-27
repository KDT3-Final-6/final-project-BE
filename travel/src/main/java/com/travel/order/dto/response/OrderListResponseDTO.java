package com.travel.order.dto.response;

import com.travel.order.entity.Order;
import com.travel.order.entity.PaymentMethod;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
public class OrderListResponseDTO {

    private LocalDate orderDate;

    private List<OrderResponseDTO> orderList;

    private PaymentMethod paymentMethod;

    @Builder
    public OrderListResponseDTO(List<OrderResponseDTO> orderList, Order order) {
        this.orderDate = LocalDate.from(orderList.get(0).getOrderDate());
        this.orderList = orderList;
        this.paymentMethod = order.getPaymentMethod();
    }
}
