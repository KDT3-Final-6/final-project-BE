package com.travel.product.exception;

import com.travel.global.exception.CustomException;
import com.travel.global.exception.CustomExceptionType;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ProductException extends CustomException {

    private final ProductExceptionType productExceptionType;


    @Override
    public CustomExceptionType getCustomExceptionType() {
        return productExceptionType;
    }
}
