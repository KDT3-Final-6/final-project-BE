package com.travel.order.service;

import com.travel.order.dto.request.OrderCreateListDTO;

public interface OrderService {

    void createOrder(OrderCreateListDTO orderCreateListDTO, String userEmail);

}
