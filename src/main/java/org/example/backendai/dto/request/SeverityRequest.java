// src/main/java/org/example/backendai/dto/request/SeverityRequest.java
package org.example.backendai.dto.request;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeverityRequest {
    private String text;                   // triệu chứng chính (admissionReason)
    private Integer age;                   // tuổi bệnh nhân
    private String gender;                 // "nam" hoặc "nữ"
    private Integer bhyt;                  // 1 = có BHYT, 0 = không
    private String past_medical_history;   // tiền sử bệnh
    private String final_diagnosis;        // chẩn đoán cuối
}