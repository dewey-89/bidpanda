package com.panda.back.global.dto;

import org.springframework.http.HttpStatus;

public class SuccessResponse extends BaseResponse {
    public SuccessResponse(String message) {
        super(HttpStatus.OK, message);
    }
}
