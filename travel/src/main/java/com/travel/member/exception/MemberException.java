package com.travel.member.exception;

import com.travel.global.exception.CustomException;
import com.travel.global.exception.CustomExceptionType;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MemberException extends CustomException {

    private final MemberExceptionType memberExceptionType;

    @Override
    public MemberExceptionType getCustomExceptionType() {
        return memberExceptionType;
    }
}
