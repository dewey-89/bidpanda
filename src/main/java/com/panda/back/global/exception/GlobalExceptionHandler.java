package com.panda.back.global.exception;

import com.panda.back.global.dto.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class GlobalExceptionHandler {
    @ExceptionHandler({ParameterValidationException.class})
    public ResponseEntity<BaseResponse> parameterValidationExHandler(
            ParameterValidationException e) {
        return ResponseEntity.badRequest()
                .body(new BaseResponse(HttpStatus.BAD_REQUEST, e.getMessage()));
    }

}
