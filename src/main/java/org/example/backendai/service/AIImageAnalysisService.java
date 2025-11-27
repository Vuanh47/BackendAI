package org.example.backendai.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.example.backendai.constant.ErrorCode;
import org.example.backendai.dto.response.MedicalEncounterResponse;
import org.example.backendai.exception.AppException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class AIImageAnalysisService {

    private final ObjectMapper objectMapper;

    @Value("${ai.image.analysis.url:https://son-riding-employ-attacks.trycloudflare.com/analyze}")
    private String aiImageAnalysisUrl;

    /**
     * Gọi AI API để phân tích ảnh bệnh án
     */
    public MedicalEncounterResponse analyzeImage(MultipartFile imageFile) {
        log.info("Starting image analysis for file: {}", imageFile.getOriginalFilename());

        // 1. Validate file
        validateImageFile(imageFile);

        // 2. Chuyển MultipartFile sang File tạm
        File tempFile = null;
        try {
            tempFile = convertMultipartFileToFile(imageFile);

            // 3. Gọi AI API
            return callAIImageAPI(tempFile);

        } catch (IOException e) {
            log.error("Error processing image file", e);
            throw new AppException(ErrorCode.FILE_PROCESSING_ERROR);
        } finally {
            // 4. Xóa file tạm
            if (tempFile != null && tempFile.exists()) {
                boolean deleted = tempFile.delete();
                if (!deleted) {
                    log.warn("Failed to delete temporary file: {}", tempFile.getAbsolutePath());
                }
            }
        }
    }

    /**
     * Validate file ảnh
     */
    private void validateImageFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            log.error("Image file is null or empty");
            throw new AppException(ErrorCode.INVALID_FILE);
        }

        // Kiểm tra định dạng file
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            log.error("Invalid file type: {}", contentType);
            throw new AppException(ErrorCode.INVALID_FILE_TYPE);
        }

        // Kiểm tra kích thước file (max 10MB)
        long maxSize = 10 * 1024 * 1024; // 10MB
        if (file.getSize() > maxSize) {
            log.error("File size exceeds limit: {} bytes", file.getSize());
            throw new AppException(ErrorCode.FILE_SIZE_EXCEEDED);
        }

        log.info("File validation passed: {} ({} bytes)",
                file.getOriginalFilename(), file.getSize());
    }

    /**
     * Chuyển MultipartFile sang File tạm
     */
    private File convertMultipartFileToFile(MultipartFile multipartFile) throws IOException {
        // Tạo file tạm trong thư mục temp của hệ thống
        Path tempDir = Files.createTempDirectory("medical-image-");
        Path tempFile = tempDir.resolve(multipartFile.getOriginalFilename());

        // Copy nội dung file
        Files.copy(multipartFile.getInputStream(), tempFile, StandardCopyOption.REPLACE_EXISTING);

        File file = tempFile.toFile();
        log.info("Created temporary file: {}", file.getAbsolutePath());

        return file;
    }

    /**
     * Gọi AI API để phân tích ảnh
     */
    private MedicalEncounterResponse callAIImageAPI(File imageFile) {
        log.info("Calling AI Image Analysis API: {}", aiImageAnalysisUrl);

        // 1. Cấu hình OkHttp client với timeout
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .build();

        // 2. Tạo RequestBody multipart/form-data
        RequestBody fileBody = RequestBody.create(
                imageFile,
                MediaType.parse("image/jpeg")
        );

        MultipartBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", imageFile.getName(), fileBody)
                .build();

        // 3. Tạo request
        Request request = new Request.Builder()
                .url(aiImageAnalysisUrl)
                .addHeader("Accept", "application/json")
                .post(requestBody)
                .build();

        // 4. Gửi request và xử lý response
        try (Response response = client.newCall(request).execute()) {
            log.info("AI API response code: {}", response.code());

            if (!response.isSuccessful()) {
                String errorBody = "";
                if (response.body() != null) {
                    errorBody = response.body().string();
                }
                log.error("AI API error: {} - {}", response.code(), errorBody);
                throw new AppException(ErrorCode.AI_IMAGE_API_ERROR);
            }

            ResponseBody responseBody = response.body();
            if (responseBody == null) {
                log.error("AI API returned empty response body");
                throw new AppException(ErrorCode.AI_IMAGE_API_NO_RESPONSE);
            }

            String jsonResponse = responseBody.string();
            log.info("AI API response: {}", jsonResponse);

            // 5. Parse JSON response
            MedicalEncounterResponse analysisResponse = objectMapper.readValue(
                    jsonResponse,
                    MedicalEncounterResponse.class
            );

            log.info("Successfully parsed AI response for patient: {}",
                    analysisResponse.getPatientName());

            return analysisResponse;

        } catch (IOException e) {
            log.error("Error calling AI Image Analysis API", e);
            throw new AppException(ErrorCode.AI_IMAGE_API_ERROR);
        }
    }
}