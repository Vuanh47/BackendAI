package org.example.backendai.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.example.backendai.constant.Gender;
import org.example.backendai.constant.UserRole;

import java.time.LocalDate;


@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PatientRegisterRequest {
     String username;
     String password;
     String fullName;
     LocalDate dateOfBirth;
     String gender;
     String address;
     String phone;

    // --- Thông tin riêng của bệnh nhân (Patient entity) ---
     String relativePhoneNumber;
     Integer managingDoctorId;
}
