package org.example.backendai.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.example.backendai.constant.SuccessCode;
import org.example.backendai.dto.response.AIResponse;
import org.example.backendai.service.AIService;
import org.example.backendai.util.ApiResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
@Slf4j
@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AIController {

     AIService service;

    @PostMapping(value = "/analyze")
    public ResponseEntity<AIResponse> analyze(@RequestParam("file") MultipartFile file) throws Exception {
        log.info("Received request to analyze file: {}", file.getOriginalFilename());
        AIResponse data = service.analyze(file);
        return ApiResponseUtil.success(data, SuccessCode.LOGIN_SUCCESS);
    }
}
