// src/main/java/org/example/backendai/service/SeverityService.java
package org.example.backendai.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.example.backendai.dto.request.SeverityRequest;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class SeverityService {

    private final ObjectMapper mapper = new ObjectMapper();

    private final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build();

    private static final String SEVERITY_API_URL = "http://127.0.0.1:8000/predict_ehr";

    public String predictSeverity(SeverityRequest request) {
        try {
            String jsonPayload = mapper.writeValueAsString(request);
            RequestBody body = RequestBody.create(jsonPayload, MediaType.get("application/json"));

            Request httpRequest = new Request.Builder()
                    .url(SEVERITY_API_URL)
                    .post(body)
                    .addHeader("Content-Type", "application/json")
                    .build();

            try (Response response = client.newCall(httpRequest).execute()) {
                String responseBody = response.body() != null ? response.body().string() : "";

                if (!response.isSuccessful()) {
                    log.warn("API phân độ trả lỗi {}: {}", response.code(), responseBody);
                    return "Không xác định";
                }

                JsonNode node = mapper.readTree(responseBody);
                String label = node.path("label").asText("Không xác định");

                return switch (label.toLowerCase()) {
                    case "nhẹ", "nhe" -> "Nhẹ";
                    case "trung bình", "trung binh", "trungbinh" -> "Trung bình";
                    case "nặng", "nang" -> "Nặng";
                    default -> "Không xác định";
                };
            }
        } catch (Exception e) {
            log.error("Lỗi khi gọi API phân độ nặng nhẹ", e);
            return "Lỗi hệ thống";
        }
    }
}