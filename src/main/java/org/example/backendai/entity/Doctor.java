package org.example.backendai.entity;
import jakarta.persistence.*;
import lombok.*;
import org.example.backendai.constant.UserRole;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(onlyExplicitlyIncluded = true)
public class Doctor {

    @Id
    private Integer DoctorID;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "DoctorID")
    private User user;

    @Column(length = 255)
    private String Specialty;

    @Column(length = 255)
    private String Department;

    @OneToMany(mappedBy = "managingDoctor")
    private Set<Patient> patients;

}