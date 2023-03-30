package com.travel.post.exception;

import com.travel.global.exception.CustomException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PostException extends CustomException {

    private final PostExceptionType postExceptionType;

    @Override
    public PostExceptionType getCustomExceptionType() {
        return postExceptionType;
    }
}
