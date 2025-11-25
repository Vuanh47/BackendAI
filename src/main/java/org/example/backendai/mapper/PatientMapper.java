package org.example.backendai.mapper;

import org.example.backendai.dto.request.PatientRegisterRequest;
import org.example.backendai.dto.request.PatientUpdateRequest;
import org.example.backendai.dto.response.MedicalEncounterResponse;
import org.example.backendai.dto.response.PatientResponse;
import org.example.backendai.entity.MedicalEncounter;
import org.example.backendai.entity.Patient;
import org.example.backendai.entity.User;
import org.mapstruct.*;
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface PatientMapper {

    @Mapping(source = "request", target = "user")
    Patient toPatient(PatientRegisterRequest request);

    @Mapping(source = "user.id", target = "id")
    @Mapping(source = "user.fullName", target = "fullName")
    @Mapping(source = "user.username", target = "username")
    @Mapping(source = "user.phone", target = "phone")
    @Mapping(source = "user.dateOfBirth", target = "dateOfBirth")
    @Mapping(source = "user.gender", target = "gender")
    @Mapping(source = "patient.relativePhoneNumber", target = "relativePhoneNumber")
    @Mapping(
            target = "managingDoctorName",
            expression = "java(patient.getManagingDoctor() != null && patient.getManagingDoctor().getUser() != null ? patient.getManagingDoctor().getUser().getFullName() : null)"
    )
    // sửa lại từ List sang 1 object
    @Mapping(source = "patient.medicalEncounter", target = "medicalEncounter")
    PatientResponse toPatientResponse(Patient patient, User user);

    @Mapping(source = "id", target = "encounterId")
    @Mapping(source = "patient.id", target = "patientId")
    @Mapping(source = "patient.user.fullName", target = "patientName")
    MedicalEncounterResponse toMedicalEncounterResponse(MedicalEncounter medicalEncounter);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "managingDoctor", ignore = true)
    void updatePatientFromRequest(PatientUpdateRequest request, @MappingTarget Patient patient);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "username", ignore = true),
            @Mapping(target = "password", ignore = true),
            @Mapping(target = "role", ignore = true)
    })
    void updateUserFromRequest(PatientUpdateRequest request, @MappingTarget User user);
}
