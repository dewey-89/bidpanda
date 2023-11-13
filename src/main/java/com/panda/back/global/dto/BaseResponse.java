package com.panda.back.global.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BaseResponse<T> {
    private static final String SUCCESS_STATUS = "success";
    private static final String ERROR_STAUTS = "error";

    private HttpStatus status;
    private String message;
    private T data;

    public BaseResponse(HttpStatus status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    //성공
    public static <T> BaseResponse<T> successData(T data) {
        return new BaseResponse<>(HttpStatus.OK, null, data);
    }

    public static <T> BaseResponse<T> successMessage(String message) {
        return new BaseResponse<>(HttpStatus.OK, message, null);
    }

    //에러
    public static <T> BaseResponse<T> error(String message) {
        return new BaseResponse<>(HttpStatus.BAD_REQUEST, message, null);
    }
}
