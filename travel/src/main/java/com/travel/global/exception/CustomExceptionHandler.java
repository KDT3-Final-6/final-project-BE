package com.travel.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class CustomExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<String> customExceptionHandler(CustomException e) {
        return ResponseEntity.status(e.getCustomExceptionType().getHttpStatus())
                .body(e.getCustomExceptionType().getErrorMsg());
    }
}
