package org.example.backendai.dto.response;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicalEncounterResponse {

    private Integer encounterId; // ID của hồ sơ khám bệnh

    private Integer patientId; // ID của bệnh nhân
    private String patientName; // Tên bệnh nhân (nếu cần hiển thị)

    private LocalDateTime admissionDate; // Ngày nhập viện
    private LocalDateTime dischargeDate; // Ngày xuất viện

    private String admissionReason; // Lý do nhập viện
    private String presentIllnessHistory; // Bệnh sử hiện tại
    private String pastMedicalHistory; // Tiền sử bệnh
    private String physicalExam; // Kết quả khám
    private String finalDiagnosis; // Chẩn đoán cuối cùng
    private String labRecommendations; // Đề xuất xét nghiệm
    private String treatmentPlan; // Kế hoạch điều trị
    private String aiSummary; // Ghi chú/tóm tắt từ AI
}
