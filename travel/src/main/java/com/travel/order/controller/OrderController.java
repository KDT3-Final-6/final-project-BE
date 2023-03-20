package com.travel.order.controller;

import com.travel.order.dto.request.OrderCreateListDTO;
import com.travel.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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

    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long orderId, String userEmail) {
        orderService.deleteOrder(orderId, userEmail);

        return ResponseEntity.ok(null);
    }
}
