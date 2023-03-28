package com.travel.wishlist.exception;

import com.travel.global.exception.CustomExceptionType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum WishlistExceptionType implements CustomExceptionType {

    ALREADY_IN_WISHLIST(HttpStatus.CONFLICT, "위시리스트에 이미 존재하는 상품입니다.");

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
