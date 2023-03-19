package com.travel.order.exception;

import com.travel.global.exception.CustomException;
import com.travel.global.exception.CustomExceptionType;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OrderException extends CustomException {

    private final OrderExceptionType productExceptionType;


    @Override
    public CustomExceptionType getCustomExceptionType() {
        return productExceptionType;
    }
}
