package org.example.backendai.mapper;

import org.example.backendai.dto.request.DoctorUpdateRequest;
import org.example.backendai.dto.request.PatientRegisterRequest;
import org.example.backendai.dto.request.UserUpdateRequest;
import org.example.backendai.dto.response.DoctorResponse;
import org.example.backendai.dto.response.UserResponse;
import org.example.backendai.entity.Doctor;
import org.example.backendai.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "role", constant = "PATIENT")
    @Mapping(source = "phone", target = "phone")
    User toUser(PatientRegisterRequest request);

    void updateUserFromRequest(UserUpdateRequest request, @MappingTarget User user);
    UserResponse toUserResponse( User user);
}
