package org.example.backendai.controller;

import lombok.RequiredArgsConstructor;
import org.example.backendai.constant.SuccessCode;
import org.example.backendai.dto.request.MessageClassifyRequest;
import org.example.backendai.dto.response.MessageClassificationResponse;
import org.example.backendai.service.MessageClassificationService;
import org.example.backendai.util.ApiResponseUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/classification")
@RequiredArgsConstructor
public class MessageClassificationController {

    private final MessageClassificationService service;

    /**
     * Phân loại mức độ nghiêm trọng của bệnh nhân bằng AI
     * Nếu bệnh nhân đã có phân loại thì update, chưa có thì tạo mới
     *
     * @param request MessageClassifyRequest chứa patientId và messageContent
     * @return MessageClassificationResponse với thông tin phân loại
     */
    @PostMapping("/classify")
    public ResponseEntity<MessageClassificationResponse> classifyMessage(
            @RequestBody MessageClassifyRequest request) {

        MessageClassificationResponse data = service.classifyMessage(
                request.getPatientId(),
                request.getMessageContent()
        );

        return ApiResponseUtil.success(data, SuccessCode.MESSAGE_CLASSIFIED);
    }

    /**
     * Lấy tình trạng phân loại hiện tại của bệnh nhân
     *
     * @param patientId ID của bệnh nhân
     * @return MessageClassificationResponse với thông tin phân loại hiện tại
     */
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<MessageClassificationResponse> getPatientClassification(
            @PathVariable Integer patientId) {

        MessageClassificationResponse data = service.getPatientClassification(patientId);

        return ApiResponseUtil.success(data, SuccessCode.CLASSIFICATION_RETRIEVED);
    }
}