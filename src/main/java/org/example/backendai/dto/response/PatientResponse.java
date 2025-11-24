package org.example.backendai.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PatientResponse {
     Integer id;
     String fullName;
     String phone;
     LocalDate dateOfBirth;
     String gender;
     String relativePhoneNumber;
     String managingDoctorName;
    MedicalEncounterResponse medicalEncounter;
}
