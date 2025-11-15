package org.example.backendai.util;

import org.example.backendai.constant.ErrorCode;
import org.example.backendai.constant.SuccessCode;
import org.example.backendai.dto.response.ApiResponse;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

public class ApiResponseUtil {
    public static <T> ResponseEntity<T> success(T data, SuccessCode successCode) {
       ApiResponse<T> response = (ApiResponse<T>) ApiResponse.builder()
                .code(successCode.getCode())
                .message(successCode.getMessage())
                .timestamp(LocalDateTime.now())
                .data(data)
                .status(successCode.getStatus())
                .build();
       return (ResponseEntity<T>) ResponseEntity.status(successCode.getStatus()).body(response);
    };

    public static <T> ResponseEntity<T> error(T data, ErrorCode errorCode) {
        ApiResponse<T> response = (ApiResponse<T>) ApiResponse.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .timestamp(LocalDateTime.now())
                .data(data)
                .status(errorCode.getStatus())
                .build();
        return (ResponseEntity<T>) ResponseEntity.status(errorCode.getStatus()).body(response);
    };


    public static ResponseEntity<ApiResponse<Void>> success(SuccessCode successCode) {
        ApiResponse response = ApiResponse.builder()
                .code(successCode.getCode())
                .message(successCode.getMessage())
                .status(successCode.getStatus())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(successCode.getStatus()).body(response);
    };

    public static ResponseEntity<ApiResponse<Void>> error(ErrorCode errorCode) {
        ApiResponse response = ApiResponse.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .status(errorCode.getStatus())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(errorCode.getStatus()).body(response);
    };
}
