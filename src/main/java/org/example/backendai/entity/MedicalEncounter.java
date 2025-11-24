package org.example.backendai.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(onlyExplicitlyIncluded = true)
public class MedicalEncounter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PatientID", nullable = false)
    private Patient patient;

    private LocalDateTime admissionDate;
    private LocalDateTime dischargeDate;

    @Lob
    private String admissionReason;

    @Lob
    private String presentIllnessHistory;

    @Lob
    private String pastMedicalHistory;

    @Lob
    private String physicalExam;

    @Lob
    private String finalDiagnosis;

    @Lob
    private String labRecommendations;

    @Lob
    private String treatmentPlan;

    @Lob
    private String aiSummary;

    private String BHYT;
}
