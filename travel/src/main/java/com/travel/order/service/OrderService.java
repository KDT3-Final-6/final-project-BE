package com.travel.order.service;

import com.travel.global.response.PageResponseDTO;
import com.travel.order.dto.request.OrderApproveDTO;
import com.travel.order.dto.request.OrderCreateListDTO;
import com.travel.order.dto.request.OrderNonMemberCreateDTO;
import org.springframework.data.domain.Pageable;

public interface OrderService {

    void createOrder(OrderCreateListDTO orderCreateListDTO, String userEmail);

    PageResponseDTO getOrders(Pageable pageable, String userEmail);

    void deleteOrder(Long orderId, String userEmail);

    PageResponseDTO getOrdersAdmin(Pageable pageable, String userEmail);

    void approveOrder(Long orderId, OrderApproveDTO orderApproveDTO, String userEmail);

    void createOrderNonMember(OrderNonMemberCreateDTO orderNonMemberCreateDTO);
}
