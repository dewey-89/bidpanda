package com.panda.back.global.exception;

public class ParameterValidationException extends RuntimeException {
    public ParameterValidationException(String message) {
        super(message);
    }
}
