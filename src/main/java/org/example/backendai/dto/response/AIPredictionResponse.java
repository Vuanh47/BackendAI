package org.example.backendai.dto.response;

import lombok.*;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AIPredictionResponse {
    private Integer patientId;
    private Integer label_id;
    private String label;
    private Double confidence;
    private Map<String, Double> probabilities;
}