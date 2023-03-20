package com.travel.order.service;

import com.travel.order.dto.request.OrderCreateListDTO;
import com.travel.order.dto.response.OrderListResponseDTO;

import java.util.List;

public interface OrderService {

    void createOrder(OrderCreateListDTO orderCreateListDTO, String userEmail);

    void deleteOrder(Long orderId, String userEmail);
}
