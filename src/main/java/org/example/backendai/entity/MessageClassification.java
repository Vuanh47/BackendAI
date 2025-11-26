package org.example.backendai.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.backendai.constant.SeverityLevel;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(onlyExplicitlyIncluded = true)
public class MessageClassification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PatientID", nullable = false, unique = true)
    private Patient patient;

    @Enumerated(EnumType.STRING)
    @Column(name = "ai_classification")
    private SeverityLevel AIClassification;

    private String confidence;

    private LocalDateTime verifiedAt;
}