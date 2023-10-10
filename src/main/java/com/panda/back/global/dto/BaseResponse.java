package com.panda.back.global.dto;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BaseResponse<T> {

    private HttpStatus httpStatus;
    private String message;
    private T data;

    public BaseResponse(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public BaseResponse(HttpStatus httpStatus, String message, T data) {
        this.httpStatus = httpStatus;
        this.message = message;
        this.data = data;
    }
}
