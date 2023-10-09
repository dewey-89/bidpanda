package com.panda.back.global.exception;

import com.amazonaws.services.kms.model.NotFoundException;
import com.panda.back.global.dto.BaseResponse;
import com.panda.back.global.dto.ErrorResponse;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({ParameterValidationException.class})
    public ResponseEntity<BaseResponse> parameterValidationExHandler(
            ParameterValidationException e) {
        return ResponseEntity.badRequest()
                .body(new BaseResponse(HttpStatus.BAD_REQUEST, e.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> illegalArgumentExHandler(
            IllegalArgumentException e) {
        return ResponseEntity.badRequest()
                .body(new ErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage()));
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ErrorResponse> jwtExHandler(
            JwtException e) {
        return ResponseEntity.badRequest()
                .body(new ErrorResponse(HttpStatus.NOT_FOUND, e.getMessage()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> runtimeExHandler(RuntimeException e) {
        return ResponseEntity.internalServerError()
                .body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponse> usernameNotFoundExHandler(
            UsernameNotFoundException e) {
        return ResponseEntity.badRequest()
                .body(new ErrorResponse(HttpStatus.NOT_FOUND, e.getMessage()));
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> NotFoundExHandler(
            NotFoundException e) {
        return ResponseEntity.badRequest()
                .body(new ErrorResponse(HttpStatus.NOT_FOUND, e.getMessage()));
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(HttpClientErrorException.Unauthorized.class)
    public ResponseEntity<ErrorResponse> UNAUTHORIZEDExHandler(
            HttpClientErrorException.Unauthorized e) {
        return ResponseEntity.badRequest()
                .body(new ErrorResponse(HttpStatus.UNAUTHORIZED, e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> exHandler(
            Exception e) {
        return ResponseEntity.internalServerError()
                .body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
    }

}
