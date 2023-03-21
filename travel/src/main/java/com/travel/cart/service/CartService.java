package com.travel.cart.service;

import com.travel.cart.dto.request.CartAddDTO;
import com.travel.cart.dto.request.CartDeleteListDTO;

public interface CartService {

    void addCart(CartAddDTO cartAddDTO, String userEmail);

    void deleteCarts(CartDeleteListDTO cartDeleteListDTO, String userEmail);
}
