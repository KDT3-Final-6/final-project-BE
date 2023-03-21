package com.travel.global.exception;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GlobalException extends CustomException {

    private final GlobalExceptionType globalExceptionType;

    @Override
    public CustomExceptionType getCustomExceptionType() {
        return globalExceptionType;
    }
}
