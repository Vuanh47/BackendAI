package org.example.backendai.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.backendai.dto.request.MedicalEncounterRequest;
import org.example.backendai.dto.response.MedicalEncounterResponse;
import org.example.backendai.dto.response.PatientResponse;
import org.example.backendai.service.MedicalEncounterService;
import org.example.backendai.util.ApiResponseUtil;
import org.example.backendai.constant.SuccessCode;
import org.springframework.http.MediaType;
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
        return ApiResponseUtil.success(data, SuccessCode.MEDICAL_ENCOUNTER_CREATED);
    }

    @GetMapping
    public ResponseEntity<List<MedicalEncounterResponse>> getAllDevices() {
        List<MedicalEncounterResponse> data = service.getAll();
        return ApiResponseUtil.success(data, SuccessCode.MEDICAL_ENCOUNTER_LISTED);
    }


    @GetMapping("/{id}")
    public ResponseEntity<MedicalEncounterResponse> getMedicalEnconter(
            @PathVariable Integer id) {

        MedicalEncounterResponse data = service.getByPatientId(id);
        return ApiResponseUtil.success(data, SuccessCode.MEDICAL_ENCOUNTER_LISTED);
    }


    @PostMapping(value = "/create-from-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MedicalEncounterResponse> createEncounterFromImage(
            @RequestParam("patientId") Integer patientId,
            @RequestParam("image") MultipartFile imageFile) {

        log.info("Received request to create encounter from image for patient: {}", patientId);
        log.info("Image file: {} ({} bytes)",
                imageFile.getOriginalFilename(),
                imageFile.getSize());

        MedicalEncounterResponse response = service
                .createEncounterFromImage(patientId, imageFile);

        return ApiResponseUtil.success(response, SuccessCode.MEDICAL_ENCOUNTER_CREATED);
    }
}
