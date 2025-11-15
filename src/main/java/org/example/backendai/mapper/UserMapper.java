package org.example.backendai.mapper;

import org.example.backendai.dto.request.PatientRegisterRequest;
import org.example.backendai.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "role", constant = "PATIENT")
    @Mapping(source = "phone", target = "phone")
    User toUser(PatientRegisterRequest request);


}
