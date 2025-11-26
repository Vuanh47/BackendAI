package org.example.backendai.service;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.example.backendai.constant.ErrorCode;
import org.example.backendai.dto.request.MedicalEncounterRequest;
import org.example.backendai.dto.response.AIPredictionResponse;
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
    AIPredictionService aiPredictionService;

    @Transactional
    public MedicalEncounterResponse createEncounter(MedicalEncounterRequest request) {
        // 1. Tìm bệnh nhân
        Patient patient = patientRepository.findById(request.getPatientId().longValue())
                .orElseThrow(() -> new AppException(ErrorCode.PATIENT_NOT_EXISTED));

        log.info("Creating encounter for patient: {}", request.getPatientId());

        // 2. Map request sang entity
        MedicalEncounter encounter = mapper.toMedicalEncounter(request);
        encounter.setPatient(patient);

        // 3. Set dates nếu null
        if (encounter.getAdmissionDate() == null) {
            encounter.setAdmissionDate(request.getAdmissionDate());
        }
        if (encounter.getDischargeDate() == null) {
            encounter.setDischargeDate(request.getDischargeDate());
        }

        // 4. Gọi AI API để dự đoán mức độ nghiêm trọng
        try {
            AIPredictionResponse prediction = aiPredictionService.predictSeverity(
                    request.getAdmissionReason(),
                    request.getPastMedicalHistory(),
                    request.getFinalDiagnosis()
            );

            // 5. Lưu kết quả dự đoán vào entity
            if (prediction != null && prediction.getLabel() != null) {
                encounter.setSeverityLevel(prediction.getLabel());
                log.info("AI predicted severity level: {} with confidence: {}",
                        prediction.getLabel(), prediction.getConfidence());
            }
        } catch (Exception e) {
            log.error("Failed to get AI prediction, continuing without severity level", e);
            // Không throw exception, tiếp tục lưu encounter
        }

        // 6. Lưu vào database
        MedicalEncounter savedEncounter = repository.save(encounter);

        log.info("Medical encounter created successfully with ID: {}", savedEncounter.getId());

        // 7. Trả về response
        return mapper.toMedicalEncounterResponse(savedEncounter);
    }

    public List<MedicalEncounterResponse> getAll() {
        List<MedicalEncounter> encounters = repository.findAll();
        return encounters.stream()
                .map(mapper::toMedicalEncounterResponse).toList();
    }
    public MedicalEncounterResponse getByPatientId(Integer patientId) {
        patientRepository.findById(Long.valueOf(patientId))
                .orElseThrow(() -> new AppException(ErrorCode.PATIENT_NOT_EXISTED));

        MedicalEncounter encounter = repository.findByPatientId(patientId).orElseThrow(
                () -> new AppException(ErrorCode.PATIENT_NOT_EXISTED)
        );

        return mapper.toMedicalEncounterResponse(encounter);
    }



}

