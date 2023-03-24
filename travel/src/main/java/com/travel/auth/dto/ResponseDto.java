package com.travel.auth.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
public class ResponseDto<T> {

    private T data;

    public ResponseDto(T data) {
        this.data = data;
    }

    public static <T> ResponseDto<T> success() {
        return new ResponseDto<>(null);
    }
}
