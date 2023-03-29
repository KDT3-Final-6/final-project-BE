package com.travel.image.exception;

import com.travel.global.exception.CustomException;
import com.travel.global.exception.CustomExceptionType;
import com.travel.global.exception.GlobalExceptionType;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ImageException extends CustomException {
    private final ImageExceptionType imageExceptionType;

    @Override
    public CustomExceptionType getCustomExceptionType() {
        return imageExceptionType;
    }
}
