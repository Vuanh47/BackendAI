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

    String API_URL = "https://court-bugs-makes-speeds.trycloudflare.com/analyze";

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
            // Đọc body một lần duy nhất, bất kể request thành công hay không
            // (Quan trọng: response.body().string() chỉ có thể được gọi MỘT LẦN)
            String jsonBody = "";
            if (response.body() != null) {
                jsonBody = response.body().string();
            }

            if (response.isSuccessful()) {
                // Log lại body khi thành công để kiểm tra
                log.info("API FastAPI trả về thành công. Body: {}", jsonBody);

                // Nếu body rỗng thì cũng là lỗi
                if (jsonBody.isEmpty()) {
                    throw new IOException("API FastAPI trả về body rỗng.");
                }

                return mapper.readValue(jsonBody, AIResponse.class);
            } else {
                // Đây là phần quan trọng nhất: Log lại LỖI mà API bên ngoài trả về
                log.error("Lỗi khi gọi API FastAPI. Code: {}", response.code());
                log.error("Nội dung lỗi từ FastAPI: {}", jsonBody); // <-- Đây là mấu chốt

                throw new IOException("Lỗi khi gọi API FastAPI: " + response.code() + ", Body: " + jsonBody);
            }

            // --- KẾT THÚC PHẦN SỬA ĐỔI ---

        } catch (Exception e) {
            // Bắt tất cả các lỗi khác (ví dụ: timeout, không kết nối được)
            log.error("Lỗi nghiêm trọng khi gọi AIService.analyze: ", e);
            throw new IOException("Lỗi khi xử lý file: " + e.getMessage(), e);
        }
    }
}