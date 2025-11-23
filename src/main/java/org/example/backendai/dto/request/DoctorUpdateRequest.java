// File: org.example.backendai.dto.request.DoctorUpdateRequest.java

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
public class DoctorUpdateRequest {
    // Thông tin USER có thể cập nhật
    String fullName;
    String phone;
    LocalDate dateOfBirth;
    String gender;

    // Thông tin DOCTOR có thể cập nhật
    String specialty;
    String department;

    // Password (nếu cần cập nhật password)
    String newPassword;
}