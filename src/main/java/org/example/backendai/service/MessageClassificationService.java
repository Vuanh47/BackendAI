package org.example.backendai.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.example.backendai.constant.ErrorCode;
import org.example.backendai.constant.SeverityLevel;
import org.example.backendai.dto.request.AIFinalRequest;
import org.example.backendai.dto.response.AIFinalResponse;
import org.example.backendai.dto.response.MessageClassificationResponse;
import org.example.backendai.entity.*;
import org.example.backendai.exception.AppException;
import org.example.backendai.mapper.MessageClassificationMapper;
import org.example.backendai.repository.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MessageClassificationService {

    PatientRepository patientRepository;
    MessageClassificationRepository repository;
    MessageClassificationMapper messageClassificationMapper;
    MessageRepository messageRepository;  // THÊM MỚI
    RestTemplate restTemplate;
    UserRepository  userRepository;
    DoctorRepository doctorRepository;

    String aiApiUrl = "http://127.0.0.1:8000/predict_final";

    public MessageClassificationResponse classifyMessage(Integer patientId, Integer doctorId) {
        log.info("Bắt đầu phân loại tin nhắn - PatientID: {}, DoctorID: {}", patientId, doctorId);

        // 1. Tìm Patient
        Patient patient = patientRepository.findById(Long.valueOf(patientId))
                .orElseThrow(() -> {
                    log.error("Không tìm thấy bệnh nhân với ID: {}", patientId);
                    return new AppException(ErrorCode.PATIENT_NOT_EXISTED);
                });

        // 2. Kiểm tra Doctor tồn tại
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> {
                    log.error("Không tìm thấy bác sĩ với ID: {}", doctorId);
                    return new AppException(ErrorCode.DOCTOR_NOT_EXISTED);
                });

        // 3. Lấy Medical Encounter từ Patient (quan hệ 1-1)
        MedicalEncounter medicalEncounter = patient.getMedicalEncounter();
        if (medicalEncounter == null) {
            log.error("Không tìm thấy hồ sơ bệnh án cho bệnh nhân ID: {}", patientId);
            throw new AppException(ErrorCode.MEDICAL_ENCOUNTER_NOT_EXISTED);
        }

        // 4. Lấy tất cả tin nhắn chưa đọc giữa bệnh nhân và bác sĩ cụ thể
        List<Message> unreadMessages = messageRepository
                .findUnreadMessagesByPatientAndDoctorId(
                        Long.valueOf(patientId),
                        Long.valueOf(doctorId));

        if (unreadMessages.isEmpty()) {
            log.warn("Không có tin nhắn chưa đọc giữa bệnh nhân ID: {} và bác sĩ ID: {}",
                    patientId, doctorId);
            throw new AppException(ErrorCode.NO_UNREAD_MESSAGES);
        }
        // 4. Nối các content lại thành chuỗi, ngăn cách bằng dấu chấm
        String messageContent = unreadMessages.stream()
                .map(Message::getContent)
                .collect(Collectors.joining(". "));

        log.info("Đã nối {} tin nhắn chưa đọc thành chuỗi: {}",
                unreadMessages.size(), messageContent);

        log.info("Thông tin bệnh án - Admission: {}, Past History: {}, Diagnosis: {}",
                medicalEncounter.getAdmissionReason(),
                medicalEncounter.getPastMedicalHistory(),
                medicalEncounter.getFinalDiagnosis());

        // 5. Tạo request để gọi AI API
        AIFinalRequest aiRequest = AIFinalRequest.builder()
                .patientId(patientId)
                .message(messageContent)  // Sử dụng chuỗi đã nối
                .admissionReason(medicalEncounter.getAdmissionReason())
                .pastMedicalHistory(medicalEncounter.getPastMedicalHistory())
                .finalDiagnosis(medicalEncounter.getFinalDiagnosis())
                .build();

        // 6. Gọi AI API
        log.info("Gọi AI API tại: {} với patientId: {}", aiApiUrl, patientId);
        AIFinalResponse aiResponse;
        try {
            aiResponse = restTemplate.postForObject(aiApiUrl, aiRequest, AIFinalResponse.class);
        } catch (Exception e) {
            log.error("Lỗi khi gọi AI API: {}", e.getMessage(), e);
            throw new AppException(ErrorCode.AI_API_ERROR);
        }

        if (aiResponse == null) {
            log.error("AI API không trả về kết quả");
            throw new AppException(ErrorCode.AI_API_NO_RESPONSE);
        }

        log.info("AI API trả về - Label: {}, Confidence: {}",
                aiResponse.getLabel(), aiResponse.getConfidence());

        // 7. Map label từ AI sang SeverityLevel enum
        SeverityLevel severityLevel;
        try {
            severityLevel = SeverityLevel.fromAiLabel(aiResponse.getLabel());
            log.info("Đã map label '{}' sang SeverityLevel: {}",
                    aiResponse.getLabel(), severityLevel.getDisplayName());
        } catch (IllegalArgumentException e) {
            log.error("Label không hợp lệ từ AI: {}", aiResponse.getLabel());
            throw new AppException(ErrorCode.INVALID_AI_LABEL);
        }

        // 8. Kiểm tra xem bệnh nhân đã có classification chưa
        MessageClassification classification = repository
                .findByPatientIdAndDoctorId(patientId, doctorId)
                .orElse(null);

        boolean isNewRecord = (classification == null);

        if (isNewRecord) {
            // Tạo mới
            log.info("Tạo mới MessageClassification cho bệnh nhân ID: {}", patientId);
            classification = MessageClassification.builder()
                    .patient(patient)
                    .doctor(doctor)
                    .AIClassification(severityLevel)
                    .confidence(String.format("%.4f", aiResponse.getConfidence()))
                    .reviewedAt(LocalDateTime.now())
                    .build();
        } else {
            // Cập nhật
            log.info("Cập nhật MessageClassification cho bệnh nhân ID: {} từ {} sang {}",
                    patientId,
                    classification.getAIClassification().getDisplayName(),
                    severityLevel.getDisplayName());
            classification.setAIClassification(severityLevel);
            classification.setConfidence(String.format("%.4f", aiResponse.getConfidence()));
            classification.setReviewedAt(LocalDateTime.now());
        }

        MessageClassification savedClassification = repository.save(classification);

        log.info("{} phân loại thành công - ID: {}, Severity: {}, Confidence: {}",
                isNewRecord ? "Tạo mới" : "Cập nhật",
                savedClassification.getId(),
                severityLevel.getDisplayName(),
                savedClassification.getConfidence());

        MessageClassificationResponse response = messageClassificationMapper
                .toMessageClassificationResponse(savedClassification);
        response.setIsNewRecord(isNewRecord);

        return response;
    }

    /**
     * Lấy tình trạng hiện tại của bệnh nhân
     */
    public MessageClassificationResponse getPatientClassification(Integer patientId) {
        log.info("Lấy tình trạng phân loại của bệnh nhân ID: {}", patientId);

        MessageClassification classification = repository
                .findByPatientId(patientId)
                .orElseThrow(() -> {
                    log.error("Chưa có phân loại cho bệnh nhân ID: {}", patientId);
                    return new AppException(ErrorCode.CLASSIFICATION_NOT_EXISTED);
                });

        MessageClassificationResponse response = messageClassificationMapper
                .toMessageClassificationResponse(classification);
        response.setIsNewRecord(false);

        return response;
    }

    public void deleteByPatientId(Integer patientId) {
        MessageClassification classification = repository.findByPatientId(patientId).orElseThrow(() ->
                new AppException(ErrorCode.CLASSIFICATION_NOT_EXISTED));

        repository.delete(classification);
    }
}