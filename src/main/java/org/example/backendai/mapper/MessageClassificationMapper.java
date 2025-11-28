package org.example.backendai.mapper;

import org.example.backendai.dto.response.MessageClassificationResponse;
import org.example.backendai.entity.MessageClassification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MessageClassificationMapper {

    @Mapping(source = "patient.id", target = "patientId")
    @Mapping(source = "doctor.id", target = "doctorId")
    @Mapping(source = "AIClassification", target = "aiClassification")
    @Mapping(source = "AIClassification.displayName", target = "aiClassificationDisplay")
    @Mapping(target = "isNewRecord", ignore = true)
    MessageClassificationResponse toMessageClassificationResponse(MessageClassification messageClassification);
}