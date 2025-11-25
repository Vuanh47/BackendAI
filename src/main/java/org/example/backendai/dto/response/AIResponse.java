package org.example.backendai.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;  // THÊM DÒNG NÀY
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)  // An toàn tuyệt đối
public class AIResponse {

    String patientName;
    String admissionDate;
    String dischargeDate;
    String admissionReason;
    String presentIllnessHistory;
    String pastMedicalHistory;
    String physicalExam;
    String finalDiagnosis;
    String labRecommendations;
    String treatmentPlan;
    String aiSummary;

    // BẮT BUỘC PHẢI DÙNG @JsonProperty vì tên field in hoa
    @JsonProperty("BHYT")
    String BHYT;
}