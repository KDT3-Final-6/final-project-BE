package com.travel.post.exception;

import com.travel.global.exception.CustomExceptionType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum PostExceptionType implements CustomExceptionType {

    INQUIRTY_TYPE_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 문의 유형입니다."),
    PRODUCT_INQUIRY_REQUIRES_PRODUCT_NUM(HttpStatus.CONFLICT, "상품문의는 상품 번호가 필요합니다.");

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
