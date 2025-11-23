package org.example.backendai.mapper;

import org.example.backendai.dto.request.DoctorRegisterRequest;
import org.example.backendai.dto.request.PatientRegisterRequest;
import org.example.backendai.dto.response.PatientResponse;
import org.example.backendai.entity.Patient;
import org.example.backendai.entity.User;
import org.mapstruct.*;
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface PatientMapper {

    @Mapping(source = "request", target = "user")
    Patient toPatient(PatientRegisterRequest request);

    @Mapping(source = "user.id", target = "id")  // CHỈ ĐỊNH RÕ ID
    @Mapping(source = "user.fullName", target = "fullName")
    @Mapping(source = "user.phone", target = "phone")
    @Mapping(source = "user.dateOfBirth", target = "dateOfBirth")
    @Mapping(source = "user.gender", target = "gender")
    @Mapping(source = "patient.relativePhoneNumber", target = "relativePhoneNumber")
    @Mapping(
            target = "managingDoctorName",
            expression = "java(patient.getManagingDoctor() != null && patient.getManagingDoctor().getUser() != null ? patient.getManagingDoctor().getUser().getFullName() : null)"
    )
    PatientResponse toPatientResponse(Patient patient, User user);
}
