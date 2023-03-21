package com.travel.cart.controller;

import com.travel.cart.dto.request.CartAddDTO;
import com.travel.cart.dto.request.CartDeleteListDTO;
import com.travel.cart.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@RequestMapping("/carts")
public class CartController {

    private final CartService cartService;

    @PostMapping
    public ResponseEntity<Void> addCart(@Valid @RequestBody CartAddDTO cartAddDTO, String userEmail) {
        cartService.addCart(cartAddDTO, userEmail);

        return ResponseEntity.ok(null);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteCarts(@Valid @RequestBody CartDeleteListDTO cartDeleteListDTO, String userEmail) {
        cartService.deleteCarts(cartDeleteListDTO, userEmail);

        return ResponseEntity.ok(null);
    }
}
