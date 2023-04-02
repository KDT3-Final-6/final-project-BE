package com.travel.member.exception;

import com.travel.global.exception.CustomExceptionType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum MemberExceptionType implements CustomExceptionType {

    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 멤버입니다."),

    MEMBER_IS_NOT_ADMIN(HttpStatus.CONFLICT, "관리자가 아닙니다."),

    NO_SURVEY_EXISTS(HttpStatus.NOT_FOUND, "해당 회원의 존재하는 설문 조사가 없습니다");

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
