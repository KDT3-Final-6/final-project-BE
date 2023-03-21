package com.travel.cart.controller;

import com.travel.cart.dto.request.CartAddDTO;
import com.travel.cart.dto.request.CartDeleteListDTO;
import com.travel.cart.service.CartService;
import com.travel.global.exception.GlobalException;
import com.travel.global.exception.GlobalExceptionType;
import com.travel.global.response.PageResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@RequestMapping("/carts")
public class CartController {

    public static final int PAGE_SIZE = 3;
    private final CartService cartService;

    @PostMapping
    public ResponseEntity<Void> addCart(@Valid @RequestBody CartAddDTO cartAddDTO, String userEmail) {
        cartService.addCart(cartAddDTO, userEmail);

        return ResponseEntity.ok(null);
    }

    @GetMapping
    public ResponseEntity<PageResponseDTO> getCarts(@RequestParam(required = false, defaultValue = "1") int page, String userEmail) {
        if (page < 1) {
            throw new GlobalException(GlobalExceptionType.PAGE_INDEX_NOT_POSITIVE_NUMBER);
        }

        PageRequest pageRequest = PageRequest.of(page - 1, PAGE_SIZE);

        PageResponseDTO carts = cartService.getCarts(pageRequest, userEmail);

        return ResponseEntity.ok(carts);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteCarts(@Valid @RequestBody CartDeleteListDTO cartDeleteListDTO, String
            userEmail) {
        cartService.deleteCarts(cartDeleteListDTO, userEmail);

        return ResponseEntity.ok(null);
    }
}
