package org.example.backendai.controller;

import lombok.RequiredArgsConstructor;
import org.example.backendai.constant.SuccessCode;
import org.example.backendai.dto.request.PasswordUpdateRequest;
import org.example.backendai.dto.request.PatientRegisterRequest;
import org.example.backendai.dto.request.PatientUpdateRequest;
import org.example.backendai.dto.request.UserUpdateRequest;
import org.example.backendai.dto.response.ApiResponse;
import org.example.backendai.dto.response.PatientResponse;
import org.example.backendai.dto.response.UserResponse;
import org.example.backendai.service.PatientService;
import org.example.backendai.service.UserService;
import org.example.backendai.util.ApiResponseUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateDoctor(
            @PathVariable Long id,
            @RequestBody UserUpdateRequest request) {

        UserResponse data = service.updateDoctor(id, request);
        return ApiResponseUtil.success(data, SuccessCode.PATIENT_UPDATED);
    }


    @PutMapping("/{id}/password")
    public ResponseEntity<ApiResponse<Void>> updatePassword(@PathVariable Long id, @RequestBody PasswordUpdateRequest request) {
        service.updatePassword(id, request);
        return ApiResponseUtil.success(SuccessCode.PASSWORD_UPDATED); // Trả về 200 OK (hoặc 204 No Content)
    }
}
