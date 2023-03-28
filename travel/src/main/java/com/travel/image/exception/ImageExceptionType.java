package com.travel.image.exception;

import com.travel.global.exception.CustomExceptionType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum ImageExceptionType implements CustomExceptionType {

    EXCEED_LIMIT_SIZE(HttpStatus.BAD_REQUEST, "파일 크기가 제한 크기인 10MB를 넘겼습니다");

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
