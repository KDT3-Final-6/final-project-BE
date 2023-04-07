package com.travel.cart.exception;

import com.travel.global.exception.CustomExceptionType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum CartExceptionType implements CustomExceptionType {

    CART_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 장바구니 상품입니다."),
    PRODUCTS_CANNOT_BE_ADDED(HttpStatus.CONFLICT, "추가할 수 없는 상품입니다."),
    DELETE_LIST_IS_AN_EMPTY_LIST(HttpStatus.BAD_REQUEST, "장바구니 삭제 리스트가 비어있습니다.");

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
