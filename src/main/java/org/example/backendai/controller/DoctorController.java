package org.example.backendai.controller;

import lombok.RequiredArgsConstructor;
import org.example.backendai.constant.SuccessCode;
import org.example.backendai.dto.request.DoctorRegisterRequest;
import org.example.backendai.dto.request.DoctorUpdateRequest;
import org.example.backendai.dto.response.DoctorResponse;
import org.example.backendai.dto.response.PatientResponse;
import org.example.backendai.service.DoctorService;
import org.example.backendai.util.ApiResponseUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
public class DoctorController {
    private final DoctorService service;

    @PostMapping("/register")
    public ResponseEntity<DoctorResponse> register(@RequestBody DoctorRegisterRequest request) {
        DoctorResponse data = service.registerDoctor(request);
        return ApiResponseUtil.success(data, SuccessCode.DOCTOR_CREATED);
    }

    @GetMapping
    public ResponseEntity<List<DoctorResponse>> getAllDevices() {
        List<DoctorResponse> data = service.getAllDoctors();
        return ApiResponseUtil.success(data, SuccessCode.DOCTOR_LISTED);
    }

    @GetMapping("/{doctorId}")
    public ResponseEntity<DoctorResponse> getDoctorID(
            @PathVariable Integer doctorId) {
        DoctorResponse data = service.getDoctorById(doctorId);
        return ApiResponseUtil.success(data, SuccessCode.PATIENT_LISTED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DoctorResponse> updateDoctor(
            @PathVariable Long id,
            @RequestBody DoctorUpdateRequest request) {

        DoctorResponse data = service.updateDoctor(id, request);
        return ApiResponseUtil.success(data, SuccessCode.DOCTOR_UPDATED);
    }


}
