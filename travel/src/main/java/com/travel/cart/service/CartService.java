package com.travel.cart.service;

import com.travel.cart.dto.CartAddDTO;

public interface CartService {

    void addCart(CartAddDTO cartAddDTO, String userEmail);
}
