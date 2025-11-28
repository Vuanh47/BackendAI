package org.example.backendai.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.backendai.constant.ClassificationStatus;
import org.example.backendai.constant.SeverityLevel;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(onlyExplicitlyIncluded = true)
@Table(name = "message_classification",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"patient_id", "doctor_id"},
                name = "uk_patient_doctor"
        ))
public class MessageClassification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @Enumerated(EnumType.STRING)
    @Column(name = "ai_classification")
    private SeverityLevel AIClassification;

    private String confidence;

    private LocalDateTime reviewedAt;

}