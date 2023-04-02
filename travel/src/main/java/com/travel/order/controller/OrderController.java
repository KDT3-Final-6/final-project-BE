package com.travel.order.controller;

import com.travel.global.exception.GlobalException;
import com.travel.global.exception.GlobalExceptionType;
import com.travel.global.response.PageResponseDTO;
import com.travel.order.dto.request.OrderCreateListDTO;
import com.travel.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    public static final int PAGE_SIZE = 10;

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<Void> createOrder(@Valid @RequestBody OrderCreateListDTO orderCreateListDTO, Authentication authentication) {
        orderService.createOrder(orderCreateListDTO, authentication.getName());

        return ResponseEntity.ok(null);
    }

    @GetMapping
    public ResponseEntity<PageResponseDTO> getOrders(@RequestParam(required = false, defaultValue = "1") int page, Authentication authentication) {
        if (page < 1) {
            throw new GlobalException(GlobalExceptionType.PAGE_INDEX_NOT_POSITIVE_NUMBER);
        }

        PageRequest pageRequest = PageRequest.of(page - 1, PAGE_SIZE);

        PageResponseDTO orders = orderService.getOrders(pageRequest, authentication.getName());

        return ResponseEntity.ok(orders);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long orderId, Authentication authentication) {
        orderService.deleteOrder(orderId, authentication.getName());

        return ResponseEntity.ok(null);
    }
}
