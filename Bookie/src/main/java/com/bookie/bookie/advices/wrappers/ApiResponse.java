package com.bookie.bookie.advices.wrappers;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ApiResponse<T> {
    private T data;
    private ApiError error;
    private boolean success;
    private LocalDateTime timestamp;
    private String path;

    // success
    public static <T> ApiResponse<T> success(T data, LocalDateTime timestamp, String path) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setData(data);
        response.setSuccess(true);
        response.setTimestamp(timestamp);
        response.setPath(path);
        return response;
    }

    // error
    public static <T> ApiResponse<T> error(ApiError error) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setError(error);
        response.setSuccess(false);
        response.setTimestamp(LocalDateTime.now());
        return response;
    }
}
