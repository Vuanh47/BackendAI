package org.example.backendai.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AIPredictionRequest {
    private String admissionReason;
    private String pastMedicalHistory;
    private String finalDiagnosis;
}