package org.example.backendai.controller;

import lombok.RequiredArgsConstructor;
import org.example.backendai.constant.SuccessCode;
import org.example.backendai.dto.request.PatientRegisterRequest;
import org.example.backendai.dto.response.MedicalEncounterResponse;
import org.example.backendai.dto.response.PatientResponse;
import org.example.backendai.service.PatientService;
import org.example.backendai.util.ApiResponseUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
public class PatientController {
    private final PatientService patientService;

    @PostMapping("/register")
    public ResponseEntity<PatientResponse> register(@RequestBody PatientRegisterRequest request) {
        PatientResponse data = patientService.registerPatient(request);
        return ApiResponseUtil.success(data, SuccessCode.PATIENT_CREATED);
    }

    @GetMapping
    public ResponseEntity<List<PatientResponse>> getAllDevices() {
        List<PatientResponse> data = patientService.getAllPatient();
        return ApiResponseUtil.success(data, SuccessCode.PATIENT_GET);
    }


}
