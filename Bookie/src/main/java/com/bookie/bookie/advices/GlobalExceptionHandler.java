package com.bookie.bookie.advices;

import com.bookie.bookie.exceptions.ResourceNotFoundException;
import com.bookie.bookie.wrappers.ApiError;
import com.bookie.bookie.wrappers.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.List;

@RestControllerAdvice("com.bookie.bookie.controllers")
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        log.warn("a MethodArgumentNotValidException occurred");
        List<String> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .toList();

        ApiError apiError = new ApiError("Validation Failed", "VALIDATION_ERROR", errors);
        ApiResponse<Object> response = ApiResponse.error(apiError);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class) // A custom exception you create
    public ResponseEntity<ApiResponse<Object>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        log.warn("A ResourceNotFoundException occurred: {}", ex.getMessage(), ex);
        ApiError apiError = new ApiError(ex.getMessage(), "RESOURCE_NOT_FOUND");
        ApiResponse<Object> response = ApiResponse.error(apiError);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGlobalExceptions(Exception ex) {
        log.error("Unhandled exception occurred: {}", ex.getMessage(), ex);
        ApiError apiError = new ApiError("An unexpected internal server error occurred.", "INTERNAL_SERVER_ERROR");
        ApiResponse<Object> response = ApiResponse.error(apiError);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}