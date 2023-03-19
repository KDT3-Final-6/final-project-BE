package com.travel.order.exception;

import com.travel.global.exception.CustomExceptionType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum OrderExceptionType implements CustomExceptionType {

    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 주문입니다."),
    MAX_CAPACITY_EXCEEDED(HttpStatus.CONFLICT, "최대 인원을 초과합니다.");

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
