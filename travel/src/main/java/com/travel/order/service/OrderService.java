package com.travel.order.service;

import com.travel.global.response.PageResponseDTO;
import com.travel.order.dto.request.OrderCreateListDTO;
import org.springframework.data.domain.Pageable;

public interface OrderService {

    void createOrder(OrderCreateListDTO orderCreateListDTO, String userEmail);

    PageResponseDTO getOrders(Pageable pageable, String userEmail);

    void deleteOrder(Long orderId, String userEmail);
}
