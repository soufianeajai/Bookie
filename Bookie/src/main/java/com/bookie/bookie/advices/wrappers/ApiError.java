package com.bookie.bookie.advices.wrappers;

import lombok.Data;

import java.util.List;

@Data
public class ApiError {
    private String code;
    private String message;
    private List<String> validationErrors;

    public ApiError(String message, String errorCode, List<String> errors) {
        this.code = errorCode;
        this.message = message;
        this.validationErrors = errors;
    }
    public ApiError(String message, String errorCode) {
        this.code = errorCode;
        this.message = message;
        this.validationErrors = null;
    }
}
