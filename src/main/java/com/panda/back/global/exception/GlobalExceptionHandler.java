package com.panda.back.global.exception;

import com.panda.back.global.dto.BaseResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({CustomException.class})
    public ResponseEntity<BaseResponse<?>> handleException(CustomException ex) {
        return ResponseEntity.status(ex.getErrorCode().getHttpStatus())
                .body(BaseResponse.error(ex.getErrorCode().getMessage()));
    }
}
