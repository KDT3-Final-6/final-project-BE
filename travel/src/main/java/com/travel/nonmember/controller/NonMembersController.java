package com.travel.nonmember.controller;

import com.travel.nonmember.service.MailService;
import com.travel.order.dto.request.OrderNonMemberCreateDTO;
import com.travel.order.entity.Order;
import com.travel.order.entity.PaymentMethod;
import com.travel.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/non-members")
public class NonMembersController {

    private final OrderService orderService;
    private final MailService mailService;

    @PostMapping("/orders")
    public ResponseEntity<Void> createOrder(@Valid @RequestBody OrderNonMemberCreateDTO orderNonMemberCreateDTO) throws MessagingException {
        Order order = orderService.createOrderNonMember(orderNonMemberCreateDTO);

        if (order.getPaymentMethod() == PaymentMethod.ACCOUNT_TRANSFER) {
            mailService.sendEmailOrder(order);
        }

        return ResponseEntity.ok(null);
    }
}
