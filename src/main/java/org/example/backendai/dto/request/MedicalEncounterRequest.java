package org.example.backendai.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MedicalEncounterRequest {
     Integer patientId; // ID của bệnh nhân
     String admissionReason;
     LocalDateTime admissionDate;
     LocalDateTime dischargeDate;
     String presentIllnessHistory;
     String pastMedicalHistory;
     String physicalExam;
     String finalDiagnosis;
     String labRecommendations;
     String treatmentPlan;

     String aiSummary; // Sẽ được gán sau khi AI xử lý
     MultipartFile imageFile; // Ảnh gửi lên từ client
}
