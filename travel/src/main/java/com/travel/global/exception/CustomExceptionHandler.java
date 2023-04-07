package com.travel.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.mail.MessagingException;

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
        BindingResult bindingResult = e.getBindingResult();

        StringBuilder builder = new StringBuilder();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            builder.append(fieldError.getField());
            builder.append("은/는 ");
            builder.append(fieldError.getDefaultMessage());
            builder.append(" 입력된 값: [");
            builder.append(fieldError.getRejectedValue());
            builder.append("]");
        }
        String errorMsg = builder.toString();

        log.error("--------------------------------");
        log.error("StackTrace = {} ", (Object) e.getStackTrace());
        log.error("HttpStatus = {} ", httpStatus);
        log.error("ErrorMsg = {} ", errorMsg);
        log.error("--------------------------------");

        return ResponseEntity.status(httpStatus)
                .body(errorMsg);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<String> missingServletRequestParameterExceptionHandler(MissingServletRequestParameterException e) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        String errorMsg = e.getParameterName() + "의 값을 넣어주세요.";

        log.error("--------------------------------");
        log.error("StackTrace = {} ", (Object) e.getStackTrace());
        log.error("HttpStatus = {} ", httpStatus);
        log.error("ErrorMsg = {} ", errorMsg);
        log.error("--------------------------------");

        return ResponseEntity.status(httpStatus)
                .body(errorMsg);
    }

    @ExceptionHandler(MessagingException.class)
    public ResponseEntity<String> messagingExceptionHandler(MessagingException e) {
        HttpStatus httpStatus = HttpStatus.CONFLICT;

        String errorMsg = "메일 전송을 실패했습니다.";

        log.error("--------------------------------");
        log.error("StackTrace = {} ", (Object) e.getStackTrace());
        log.error("HttpStatus = {} ", httpStatus);
        log.error("ErrorMsg = {} ", errorMsg);
        log.error("--------------------------------");

        return ResponseEntity.status(httpStatus)
                        .body(errorMsg);
    }
}
