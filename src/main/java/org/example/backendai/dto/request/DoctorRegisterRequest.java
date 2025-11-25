package org.example.backendai.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.example.backendai.constant.Department;
import org.example.backendai.constant.Gender;
import org.example.backendai.constant.UserRole;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DoctorRegisterRequest {

    // --- Thông tin chung (User entity) ---
    String username;
    String password;
    String fullName;
    LocalDate dateOfBirth;
    Gender gender;
    String address;
    String phone;

    // --- Thông tin riêng của bác sĩ (Doctor entity) ---
    String specialty;   // Chuyên khoa

    Department department;
}
