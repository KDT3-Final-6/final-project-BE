package com.travel.order.controller;

import com.travel.order.dto.request.OrderCreateListDTO;
import com.travel.order.dto.response.OrderListResponseDTO;
import com.travel.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<Void> createOrder(@Valid @RequestBody OrderCreateListDTO orderCreateListDTO, String userEmail) {
        orderService.createOrder(orderCreateListDTO, userEmail);

        return ResponseEntity.ok(null);
    }
}
