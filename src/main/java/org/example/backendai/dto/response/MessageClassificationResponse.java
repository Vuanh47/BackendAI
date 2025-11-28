package org.example.backendai.dto.response;

import lombok.*;
import org.example.backendai.constant.SeverityLevel;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageClassificationResponse {
    private Integer id;
    private Integer patientId;
    private Integer doctorId;
    private SeverityLevel aiClassification;
    private String aiClassificationDisplay;
    private String confidence;
    private LocalDateTime verifiedAt;
    private Boolean isNewRecord;
}