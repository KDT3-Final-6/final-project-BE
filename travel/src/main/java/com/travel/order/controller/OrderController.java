package com.travel.order.controller;

import com.travel.global.response.PageResponseDTO;
import com.travel.order.dto.request.OrderCreateListDTO;
import com.travel.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    public static final int PAGE_SIZE = 3;

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<Void> createOrder(@Valid @RequestBody OrderCreateListDTO orderCreateListDTO, String userEmail) {
        orderService.createOrder(orderCreateListDTO, userEmail);

        return ResponseEntity.ok(null);
    }

    @GetMapping
    public ResponseEntity<PageResponseDTO> getOrders(@RequestParam(required = false, defaultValue = "1") int page, String userEmail) {
        PageRequest pageRequest;

        try {
            pageRequest = PageRequest.of(page - 1, PAGE_SIZE);
            //정상적인 범위 내의 페이지 번호면 해당 페이지로
        } catch (IllegalArgumentException e) {
            pageRequest = PageRequest.of(0, PAGE_SIZE);
            //음수나 오버플로 발생시키는 페이지 번호면 0번페이지로
        }

        PageResponseDTO orders = orderService.getOrders(pageRequest, userEmail);

        return ResponseEntity.ok(orders);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long orderId, String userEmail) {
        orderService.deleteOrder(orderId, userEmail);

        return ResponseEntity.ok(null);
    }
}
