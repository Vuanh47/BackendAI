package org.example.backendai.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.backendai.dto.request.MedicalEncounterRequest;
import org.example.backendai.dto.response.MedicalEncounterResponse;
import org.example.backendai.dto.response.PatientResponse;
import org.example.backendai.service.MedicalEncounterService;
import org.example.backendai.util.ApiResponseUtil;
import org.example.backendai.constant.SuccessCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/encounter")
@RequiredArgsConstructor
@Slf4j
public class MedicalEncounterController {

    private final MedicalEncounterService service;

    @PostMapping("/create")
    public ResponseEntity<MedicalEncounterResponse> register(@RequestBody MedicalEncounterRequest request) {
        MedicalEncounterResponse data = service.createEncounter(request);
        return ApiResponseUtil.success(data, SuccessCode.DOCTOR_CREATED);
    }

    @GetMapping
    public ResponseEntity<List<MedicalEncounterResponse>> getAllDevices() {
        List<MedicalEncounterResponse> data = service.getAll();
        return ApiResponseUtil.success(data, SuccessCode.MEDICAL_ENCOUNTER_LISTED);
    }

//    @GetMapping("/{doctorId}/patients")
//    public ResponseEntity<List<PatientResponse>> getAllPatientsByDoctor(
//            @PathVariable Integer doctorId) {
//        List<PatientResponse> data = service.(doctorId);
//        return ApiResponseUtil.success(data, SuccessCode.PATIENT_LISTED);
//    }


}
