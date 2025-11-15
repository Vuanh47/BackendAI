package org.example.backendai.exception;

import lombok.Builder;
import org.example.backendai.constant.ErrorCode;
import org.example.backendai.dto.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@Builder
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse> handleAppException(AppException exception) {
        ErrorCode error = exception.getErrorCode();
        ApiResponse apiResponse = ApiResponse.builder()
                .code(error.getCode())
                .message(error.getMessage())
                .status(error.getStatus())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.badRequest().body(apiResponse);
    }
}
