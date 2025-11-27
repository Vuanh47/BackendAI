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
import org.example.backendai.entity.MedicalEncounter;
import org.example.backendai.entity.Message;
import org.example.backendai.entity.MessageClassification;
import org.example.backendai.entity.Patient;
import org.example.backendai.exception.AppException;
import org.example.backendai.mapper.MessageClassificationMapper;
import org.example.backendai.repository.MessageClassificationRepository;
import org.example.backendai.repository.MessageRepository;
import org.example.backendai.repository.PatientRepository;
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


    String aiApiUrl = "http://127.0.0.1:8000/predict_final";

    public MessageClassificationResponse classifyMessage(Integer patientId) {
        log.info("Bắt đầu phân loại tin nhắn - PatientID: {}", patientId);

        // 1. Tìm Patient
        Patient patient = patientRepository.findById(Long.valueOf(patientId))
                .orElseThrow(() -> {
                    log.error("Không tìm thấy bệnh nhân với ID: {}", patientId);
                    return new AppException(ErrorCode.PATIENT_NOT_EXISTED);
                });

        // 2. Lấy Medical Encounter từ Patient (quan hệ 1-1)
        MedicalEncounter medicalEncounter = patient.getMedicalEncounter();
        if (medicalEncounter == null) {
            log.error("Không tìm thấy hồ sơ bệnh án cho bệnh nhân ID: {}", patientId);
            throw new AppException(ErrorCode.MEDICAL_ENCOUNTER_NOT_EXISTED);
        }

        // 3. Lấy tất cả tin nhắn chưa đọc của bệnh nhân
        List<Message> unreadMessages = messageRepository
                .findUnreadMessagesByPatientId(Long.valueOf(patientId));

        if (unreadMessages.isEmpty()) {
            log.warn("Không có tin nhắn chưa đọc cho bệnh nhân ID: {}", patientId);
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
                .findByPatientId(patientId)
                .orElse(null);

        boolean isNewRecord = (classification == null);

        if (isNewRecord) {
            // Tạo mới
            log.info("Tạo mới MessageClassification cho bệnh nhân ID: {}", patientId);
            classification = MessageClassification.builder()
                    .patient(patient)
                    .AIClassification(severityLevel)
                    .confidence(String.format("%.4f", aiResponse.getConfidence()))
                    .verifiedAt(LocalDateTime.now())
                    .build();
        } else {
            // Cập nhật
            log.info("Cập nhật MessageClassification cho bệnh nhân ID: {} từ {} sang {}",
                    patientId,
                    classification.getAIClassification().getDisplayName(),
                    severityLevel.getDisplayName());
            classification.setAIClassification(severityLevel);
            classification.setConfidence(String.format("%.4f", aiResponse.getConfidence()));
            classification.setVerifiedAt(LocalDateTime.now());
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