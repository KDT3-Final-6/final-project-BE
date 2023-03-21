package com.travel.global.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum GlobalExceptionType implements CustomExceptionType {

    PAGE_INDEX_NOT_POSITIVE_NUMBER(HttpStatus.BAD_REQUEST, "Page는 0보다 커야합니다.");
    private final HttpStatus httpStatus;
    private final String errorMsg;

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
    @Override
    public String getErrorMsg() {
        return errorMsg;
    }
}