package com.travel.global.exception;

import org.springframework.http.HttpStatus;

public interface CustomExceptionType {

    HttpStatus getHttpStatus();
    String getErrorMsg();
}
