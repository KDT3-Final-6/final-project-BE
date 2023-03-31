package com.travel.nonmember.controller;

import com.travel.order.dto.request.OrderNonMemberCreateDTO;
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
@RequestMapping("/non-members")
public class NonMembersController {

    private final OrderService orderService;

    @PostMapping("/orders")
    public ResponseEntity<Void> createOrder(@Valid @RequestBody OrderNonMemberCreateDTO orderNonMemberCreateDTO) {
        orderService.createOrderNonMember(orderNonMemberCreateDTO);

        return ResponseEntity.ok(null);
    }
}
