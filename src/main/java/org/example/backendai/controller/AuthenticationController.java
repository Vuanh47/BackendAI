package org.example.backendai.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.backendai.constant.SuccessCode;
import org.example.backendai.dto.request.AuthenticationRequest;
import org.example.backendai.dto.request.PatientRegisterRequest;
import org.example.backendai.dto.response.AuthenticationResponse;
import org.example.backendai.dto.response.PatientResponse;
import org.example.backendai.service.AuthenticationService;
import org.example.backendai.service.PatientService;
import org.example.backendai.util.ApiResponseUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/authenticate")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AuthenticationService service;
    @PostMapping
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        AuthenticationResponse data =  service.authenticate(request);
        return ApiResponseUtil.success(data, SuccessCode.LOGIN_SUCCESS);
    }

}
