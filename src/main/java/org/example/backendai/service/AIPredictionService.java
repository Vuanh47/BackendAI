package org.example.backendai.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.backendai.dto.request.AIPredictionRequest;
import org.example.backendai.dto.response.AIPredictionResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class AIPredictionService {

    private final RestTemplate restTemplate;

    @Value("${ai.prediction.url:http://127.0.0.1:8000/predict_ehr}")
    private String aiPredictionUrl;

    public AIPredictionResponse predictSeverity(String admissionReason,
                                                String pastMedicalHistory,
                                                String finalDiagnosis) {
        try {
            AIPredictionRequest request = AIPredictionRequest.builder()
                    .admissionReason(admissionReason)
                    .pastMedicalHistory(pastMedicalHistory)
                    .finalDiagnosis(finalDiagnosis)
                    .build();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<AIPredictionRequest> entity = new HttpEntity<>(request, headers);

            log.info("Calling AI Prediction API with data: {}", request);

            ResponseEntity<AIPredictionResponse> response = restTemplate.exchange(
                    aiPredictionUrl,
                    HttpMethod.POST,
                    entity,
                    AIPredictionResponse.class
            );

            log.info("AI Prediction response: {}", response.getBody());

            return response.getBody();
        } catch (Exception e) {
            log.error("Error calling AI Prediction API", e);
            // Trả về giá trị mặc định nếu API lỗi
            return AIPredictionResponse.builder()
                    .label("unknown")
                    .confidence(0.0)
                    .build();
        }
    }
}