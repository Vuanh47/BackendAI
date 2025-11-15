package org.example.backendai.mapper;

import org.example.backendai.dto.request.DoctorRegisterRequest;
import org.example.backendai.dto.request.PatientRegisterRequest;
import org.example.backendai.dto.response.DoctorResponse;
import org.example.backendai.dto.response.PatientResponse;
import org.example.backendai.entity.Doctor;
import org.example.backendai.entity.Patient;
import org.example.backendai.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface DoctorMapper {
    @Mapping(source = "request", target = "user") // gọi sang UserMapper
    Doctor toDoctor(DoctorRegisterRequest request);
    User toUser(DoctorRegisterRequest request);
    DoctorResponse toDoctorResponse(Doctor doctor, User user);
    DoctorResponse toDoctorResponseSimple(Doctor doctor);

}

