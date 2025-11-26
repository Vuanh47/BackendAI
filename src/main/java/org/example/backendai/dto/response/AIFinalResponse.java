package org.example.backendai.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AIFinalResponse {
    private Integer patientId;

    @JsonProperty("label_id")
    private Integer labelId;

    private String label;
    private Double confidence;
    private Map<String, Double> probabilities;
}