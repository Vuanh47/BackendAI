package org.example.backendai.service;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.example.backendai.constant.ErrorCode;
import org.example.backendai.dto.request.MedicalEncounterRequest;
import org.example.backendai.dto.response.MedicalEncounterResponse;
import org.example.backendai.dto.response.PatientResponse;
import org.example.backendai.entity.MedicalEncounter;
import org.example.backendai.entity.Patient;
import org.example.backendai.exception.AppException;
import org.example.backendai.mapper.MedicalEncounterMapper;
import org.example.backendai.repository.MedicalEncounterRepository;
import org.example.backendai.repository.PatientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MedicalEncounterService {

    MedicalEncounterMapper mapper;
    MedicalEncounterRepository repository;
    PatientRepository patientRepository;

    public MedicalEncounterResponse createEncounter(MedicalEncounterRequest request) {
        Patient patient = patientRepository.findById(request.getPatientId().longValue())
                .orElseThrow(() -> new AppException(ErrorCode.PATIENT_NOT_EXISTED));

        log.info(request.toString() + "ok");
        MedicalEncounter encounter = mapper.toMedicalEncounter(request);

        encounter.setPatient(patient);
        if (encounter.getAdmissionDate() == null) {
            encounter.setAdmissionDate(request.getAdmissionDate());
        }
        if (encounter.getDischargeDate() == null) {
            encounter.setDischargeDate(request.getDischargeDate());
        }
        repository.save(encounter);
        return mapper.toMedicalEncounterResponse(encounter);
    }
    public List<MedicalEncounterResponse> getAll() {
        List<MedicalEncounter> encounters = repository.findAll();
        return encounters.stream()
                .map(mapper::toMedicalEncounterResponse).toList();
    }



}

