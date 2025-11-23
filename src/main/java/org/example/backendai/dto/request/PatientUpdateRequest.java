
package org.example.backendai.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.example.backendai.constant.Gender;

import java.time.LocalDate;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PatientUpdateRequest {
    String fullName;
    String phone;
    LocalDate dateOfBirth;
    String gender;
    String newPassword;

    String relativePhoneNumber;
    Long managingDoctorId; // ID của bác sĩ quản lý mới
}