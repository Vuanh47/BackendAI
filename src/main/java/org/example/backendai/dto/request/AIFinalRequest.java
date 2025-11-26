package org.example.backendai.dto.request;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AIFinalRequest {
    Integer patientId;
    String message;
    String admissionReason;
    String pastMedicalHistory;
    String finalDiagnosis;
}
