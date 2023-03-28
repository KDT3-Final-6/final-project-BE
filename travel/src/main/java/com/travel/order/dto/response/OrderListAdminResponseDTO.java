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
public class OrderListAdminResponseDTO {

    private LocalDate orderDate;

    private List<OrderAdminResponseDTO> orderList;

    private String paymentMethod;

    @Builder
    public OrderListAdminResponseDTO(List<OrderAdminResponseDTO> orderList, Order order) {
        this.orderDate = LocalDate.from(orderList.get(0).getOrderDate());
        this.orderList = orderList;
        this.paymentMethod = order.getPaymentMethod().getKorean();
    }
}
