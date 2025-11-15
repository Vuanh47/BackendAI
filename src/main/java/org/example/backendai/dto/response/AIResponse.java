package org.example.backendai.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
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
}
