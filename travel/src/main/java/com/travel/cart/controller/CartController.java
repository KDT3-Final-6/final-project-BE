package com.travel.cart.controller;

import com.travel.cart.dto.request.CartAddListDTO;
import com.travel.cart.dto.request.CartUpdateDTO;
import com.travel.cart.exception.CartException;
import com.travel.cart.exception.CartExceptionType;
import com.travel.cart.service.CartService;
import com.travel.global.exception.GlobalException;
import com.travel.global.exception.GlobalExceptionType;
import com.travel.global.response.PageResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/carts")
public class CartController {

    public static final int PAGE_SIZE = 10;
    private final CartService cartService;

    @PostMapping
    public ResponseEntity<Void> addCart(@Valid @RequestBody CartAddListDTO cartAddListDTO, Authentication authentication) {
        cartService.addCart(cartAddListDTO, authentication.getName());

        return ResponseEntity.ok(null);
    }

    @GetMapping
    public ResponseEntity<PageResponseDTO> getCarts(@RequestParam(required = false, defaultValue = "1") int page, Authentication authentication) {
        if (page < 1) {
            throw new GlobalException(GlobalExceptionType.PAGE_INDEX_NOT_POSITIVE_NUMBER);
        }

        PageRequest pageRequest = PageRequest.of(page - 1, PAGE_SIZE);
        PageResponseDTO carts = cartService.getCarts(pageRequest, authentication.getName());

        return ResponseEntity.ok(carts);
    }

    @PatchMapping("/{cartId}")
    public ResponseEntity<Void> updateCart(@PathVariable Long cartId, @Valid @RequestBody CartUpdateDTO cartUpdateDTO, Authentication authentication) {
        cartService.updateCart(cartId, cartUpdateDTO, authentication.getName());

        return ResponseEntity.ok(null);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteCarts(@RequestParam List<Long> cartIds, Authentication authentication) {
        if (cartIds.isEmpty()) {
            throw new CartException(CartExceptionType.DELETE_LIST_IS_AN_EMPTY_LIST);
        }

        cartService.deleteCarts(cartIds, authentication.getName());

        return ResponseEntity.ok(null);
    }
}
