package org.example.backendai.mapper;

import org.example.backendai.dto.request.AuthenticationRequest;
import org.example.backendai.dto.request.PatientRegisterRequest;
import org.example.backendai.dto.response.AuthenticationResponse;
import org.example.backendai.dto.response.PatientResponse;
import org.example.backendai.entity.Patient;
import org.example.backendai.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface AuthenticationMapper {


    @Mapping(target = "role", expression = "java(user.getRole() != null ? user.getRole().name() : null)")
    @Mapping(target = "success", constant = "true")
    AuthenticationResponse toAuthenticationResponse(User user);
}

