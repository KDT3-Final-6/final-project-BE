package com.travel.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class CustomExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<String> customExceptionHandler(CustomException e) {
        log.error("--------------------------------");
        log.error("StackTrace = {} ", (Object) e.getStackTrace());
        log.error("HttpStatus = {} ", e.getCustomExceptionType().getHttpStatus());
        log.error("ErrorMsg = {} ", e.getCustomExceptionType().getErrorMsg());
        log.error("--------------------------------");

        return ResponseEntity.status(e.getCustomExceptionType().getHttpStatus())
                .body(e.getCustomExceptionType().getErrorMsg());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        String errorMsg = "잘못된 값입니다.";

        log.error("--------------------------------");
        log.error("StackTrace = {} ", (Object) e.getStackTrace());
        log.error("HttpStatus = {} ", httpStatus);
        log.error("ErrorMsg = {} ", errorMsg);
        log.error("--------------------------------");

        return ResponseEntity.status(httpStatus)
                .body(errorMsg);
    }
}
