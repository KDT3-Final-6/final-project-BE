package com.travel.order.controller;

import com.travel.order.dto.request.OrderCreateListDTO;
import com.travel.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<String> createOrder(@Valid @RequestBody OrderCreateListDTO orderCreateListDTO) {
        String userEmail = "test@test.com";
        orderService.createOrder(orderCreateListDTO, userEmail);

        return ResponseEntity.ok(null);
    }
}
