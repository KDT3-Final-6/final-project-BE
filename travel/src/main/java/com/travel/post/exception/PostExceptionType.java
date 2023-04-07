package com.travel.post.exception;

import com.travel.global.exception.CustomExceptionType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum PostExceptionType implements CustomExceptionType {

    INQUIRTY_TYPE_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 문의 유형입니다."),
    PRODUCT_INQUIRY_REQUIRES_PRODUCT_NUM(HttpStatus.CONFLICT, "상품문의는 상품 번호가 필요합니다."),
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 포스트입니다."),
    NOT_THE_PRODUCT_ORDERED(HttpStatus.CONFLICT, "주문한 상품이 아닙니다."),
    CAN_ONLY_WRITE_ONE_REVIEW(HttpStatus.CONFLICT, "리뷰는 하나만 작성할 수 있습니다.");

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
