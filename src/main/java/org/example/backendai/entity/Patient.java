package org.example.backendai.entity;
import jakarta.persistence.*;
import lombok.*;
import org.example.backendai.constant.UserRole;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;


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
    private String RelativePhoneNumber;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ManagingDoctorID")
    private Doctor managingDoctor;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    private List<MedicalEncounter> medicalEncounters;
}