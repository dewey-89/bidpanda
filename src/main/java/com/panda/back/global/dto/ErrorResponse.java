package com.panda.back.global.dto;

import org.springframework.http.HttpStatus;

public class ErrorResponse extends BaseResponse {
    public ErrorResponse(HttpStatus HttpStatus, String message) {
        super(HttpStatus, message);
    }
}
