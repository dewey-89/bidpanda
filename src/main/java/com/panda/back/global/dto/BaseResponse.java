package com.panda.back.global.dto;

import org.springframework.http.HttpStatus;

public class BaseResponse {

    private HttpStatus httpStatus;
    private String message;

    public BaseResponse(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
