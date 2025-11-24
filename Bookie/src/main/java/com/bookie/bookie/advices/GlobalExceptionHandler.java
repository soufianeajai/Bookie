package com.bookie.bookie.advices;

import com.bookie.bookie.exceptions.ResourceNotFoundException;
import com.bookie.bookie.advices.wrappers.ApiError;
import com.bookie.bookie.advices.wrappers.ApiResponse;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice()
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest request) {
        log.warn("a MethodArgumentNotValidException occurred");
        List<String> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .toList();

        ApiError apiError = new ApiError("Validation Failed", "VALIDATION_ERROR", errors);
        ApiResponse<Object> response = ApiResponse.error(apiError);
        response.setPath(request.getRequestURI());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleResourceNotFoundException(ResourceNotFoundException ex, HttpServletRequest request) {
        log.warn("A ResourceNotFoundException occurred: {}", ex.getMessage(), ex);
        ApiError apiError = new ApiError(ex.getMessage(), "RESOURCE_NOT_FOUND");
        ApiResponse<Object> response = ApiResponse.error(apiError);
        response.setPath(request.getRequestURI());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<Object>> handleDataConflict(DataIntegrityViolationException ex, HttpServletRequest request) {
        log.warn("Data integrity violation: {}", ex.getMessage());
        ApiError apiError = new ApiError("The resource could not be created. It conflicts with existing data.", "DATABASE INTEGRITY VIOLATION");
        ApiResponse<Object> response = ApiResponse.error(apiError);
        response.setPath(request.getRequestURI());
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Object>> handleJwtException(ConstraintViolationException ex, HttpServletRequest request) {
        log.warn("A ConstraintViolationException occurred: {}", ex.getMessage(), ex);
        List<String> errors = ex.getConstraintViolations().stream()
                .map(cv -> {
                    String fullPath = cv.getPropertyPath().toString();
                    String fieldName = fullPath.substring(fullPath.lastIndexOf('.') + 1);
                    return fieldName + ": " + cv.getMessage();
                })
                .toList();
        ApiError apiError = new ApiError("Validation Failed", "VALIDATION_ERROR", errors);
        ApiResponse<Object> response = ApiResponse.error(apiError);
        response.setPath(request.getRequestURI());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Object>> handleJwtException(IllegalArgumentException ex, HttpServletRequest request) {
        String error = ex.getMessage();
        log.warn("A IllegalArgumentException occurred: {}", error, ex);
        ApiError apiError = new ApiError("Validation Failed", "VALIDATION_ERROR", error.lines().toList());
        ApiResponse<Object> response = ApiResponse.error(apiError);
        response.setPath(request.getRequestURI());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(InvalidDataAccessApiUsageException.class)
    public ResponseEntity<ApiResponse<Object>> handleJwtException(InvalidDataAccessApiUsageException ex, HttpServletRequest request) {
        String error = ex.getMessage();
        log.warn("A InvalidDataAccessApiUsageException occurred: {}", error, ex);
        ApiError apiError = new ApiError("Validation Failed", "VALIDATION_ERROR", error.lines().toList());
        ApiResponse<Object> response = ApiResponse.error(apiError);
        response.setPath(request.getRequestURI());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Object>> handleMessageNotReadable(
            HttpMessageNotReadableException ex, HttpServletRequest request) {

        log.warn("Malformed JSON request: {}", ex.getMessage());

        String genericMessage = "The request body is malformed or contains invalid data types.";
        String errorCode = "BAD_REQUEST_JSON";

        Throwable cause = ex.getCause();
        if (cause instanceof InvalidFormatException ife) {
            String fieldPath = ife.getPath().stream()
                    .map(JsonMappingException.Reference::getFieldName)
                    .filter(java.util.Objects::nonNull)
                    .collect(Collectors.joining("."));
            String expectedType = ife.getTargetType().getSimpleName();
            if (!fieldPath.isEmpty()) {
                genericMessage = String.format(
                        "Invalid value for field '%s'. Expected a value of type '%s'.",
                        fieldPath, expectedType
                );
            }
        }
        ApiError apiError = new ApiError(genericMessage, errorCode);
        ApiResponse<Object> response = ApiResponse.error(apiError);
        response.setPath(request.getRequestURI());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiResponse<Object>> handleIllegalStateException(IllegalStateException ex, HttpServletRequest req){
        String error = ex.getMessage();
        log.warn("A IllegalStateException occurred: {}", error, ex);
        ApiError apiError = new ApiError("Validation Failed", "VALIDATION_ERROR", error.lines().toList());
        ApiResponse<Object> response = ApiResponse.error(apiError);
        response.setPath(req.getRequestURI());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponse<Object>> handleAuthenticationException(AuthenticationException ex, HttpServletRequest request){
        String error = ex.getMessage();
        log.warn("A AuthenticationException occurred: {}", error, ex);
        ApiError apiError = new ApiError("Authentication Failed", "UNAUTHORIZED", error.lines().toList());
        ApiResponse<Object> response = ApiResponse.error(apiError);
        response.setPath(request.getRequestURI());
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ApiResponse<Object>> handleJwtException(JwtException ex, HttpServletRequest request) {
        String error = ex.getMessage();
        log.warn("A JwtException occurred: {}", error, ex);
        ApiError apiError = new ApiError("Authentication Failed", "UNAUTHORIZED", error.lines().toList());
        ApiResponse<Object> response = ApiResponse.error(apiError);
        response.setPath(request.getRequestURI());
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGlobalExceptions(Exception ex, HttpServletRequest request) {
        log.error("Unhandled exception occurred: {}", ex.getMessage(), ex);
        ApiError apiError = new ApiError("An unexpected internal server error occurred.", "INTERNAL_SERVER_ERROR");
        ApiResponse<Object> response = ApiResponse.error(apiError);
        response.setPath(request.getRequestURI());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }



}

