package org.example.backendai.entity;
import jakarta.persistence.*;
import lombok.*;
import org.example.backendai.constant.UserRole;
import java.time.LocalDate;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MessageID", nullable = false)
    private Message message;

    @Column(columnDefinition = "enum('Khẩn', 'Trung bình', 'Nguy cấp')")
    private String AIClassification;

    @Lob
    private String AIReasoning;

    @Column(columnDefinition = "enum('Khẩn', 'Trung bình', 'Nguy cấp')")
    private String DoctorVerification;

    private LocalDateTime VerifiedAt;


}
