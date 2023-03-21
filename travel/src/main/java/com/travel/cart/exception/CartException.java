package com.travel.cart.exception;

import com.travel.global.exception.CustomException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CartException extends CustomException {

    private final CartExceptionType cartExceptionType;

    @Override
    public CartExceptionType getCustomExceptionType() {
        return cartExceptionType;
    }
}
