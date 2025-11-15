package org.example.backendai.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.example.backendai.dto.response.AIResponse;
import org.example.backendai.dto.response.MedicalEncounterResponse;
import org.example.backendai.entity.MedicalEncounter;
import org.example.backendai.mapper.MedicalEncounterMapper;
import org.example.backendai.repository.MedicalEncounterRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AIService {
    MedicalEncounterRepository repository;
    MedicalEncounterMapper medicalEncounterMapper;
    OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS) // Thời gian chờ kết nối
            .writeTimeout(30, TimeUnit.SECONDS)   // Thời gian chờ ghi
            .readTimeout(120, TimeUnit.SECONDS)  // THỜI GIAN CHỜ ĐỌC (QUAN TRỌNG NHẤT)
            .build();

    String API_URL = "https://unsanitized-alesia-culinarily.ngrok-free.dev/analyze";
    ObjectMapper mapper = new ObjectMapper();

    public AIResponse analyze(MultipartFile file) throws IOException {
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getOriginalFilename(),
                        RequestBody.create(file.getBytes(),
                                MediaType.parse(file.getContentType() != null ? file.getContentType() : "image/jpeg")))
                .build();

              Request request = new Request.Builder()
                .url(API_URL)
                .post(requestBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String json = response.body().string();
//                MedicalEncounter data =  mapper.readValue(json, MedicalEncounter.class);
////                repository.save(data);
//                return medicalEncounterMapper.toMedicalEncounterResponse(data);
                return mapper.readValue(json, AIResponse.class);
            } else {
                throw new IOException("Lỗi khi gọi API FastAPI: " + response.code());
            }
        }
    }
}
