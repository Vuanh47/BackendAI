package org.example.backendai.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageClassifyRequest {
    private Integer patientId;

}