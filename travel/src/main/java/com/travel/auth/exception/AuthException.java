package com.travel.auth.exception;

import com.travel.global.exception.CustomException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AuthException extends CustomException {

    private final AuthExceptionType authExceptionType;

    @Override
    public AuthExceptionType getCustomExceptionType() {
        return authExceptionType;
    }
}