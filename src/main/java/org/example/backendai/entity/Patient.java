package org.example.backendai.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(onlyExplicitlyIncluded = true)
public class Patient {

    @Id
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "PatientID")
    private User user;

    @Column(length = 20)
    private String relativePhoneNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ManagingDoctorID")
    private Doctor managingDoctor;

    // 1 patient <-> 1 medical encounter
    @OneToOne(mappedBy = "patient", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private MedicalEncounter medicalEncounter;
}
