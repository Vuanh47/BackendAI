//package org.example.backendai.controller;
//
//import lombok.AccessLevel;
//import lombok.RequiredArgsConstructor;
//import lombok.experimental.FieldDefaults;
//import lombok.extern.slf4j.Slf4j;
//import org.example.backendai.constant.SuccessCode;
//import org.example.backendai.dto.response.AIResponse;
//import org.example.backendai.util.ApiResponseUtil;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//@Slf4j
//@RestController
//@RequestMapping("/api/ai")
//@RequiredArgsConstructor
//@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
//public class AIController {
//
//     AIService service;
//
//    @PostMapping(value = "/analyze", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponseEntity<AIResponse> analyze(@RequestPart("file") MultipartFile file) throws Exception {
//        log.info("=== Bắt đầu xử lý file ===");
//        log.info("Tên file: {}", file.getOriginalFilename());
//        log.info("Content-Type: {}", file.getContentType());
//        log.info("Kích thước: {} bytes", file.getSize());
//
//        if (file == null || file.isEmpty()) {
//            throw new IllegalArgumentException("File không được để trống");
//        }
//
//        AIResponse data = service.analyze(file);
//        return ApiResponseUtil.success(data, SuccessCode.LOGIN_SUCCESS); // hoặc code phù hợp
//    }
//}
