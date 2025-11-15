package org.example.backendai.mapper;

import org.example.backendai.dto.request.DoctorRegisterRequest;
import org.example.backendai.dto.request.MedicalEncounterRequest;
import org.example.backendai.dto.response.DoctorResponse;
import org.example.backendai.dto.response.MedicalEncounterResponse;
import org.example.backendai.entity.Doctor;
import org.example.backendai.entity.MedicalEncounter;
import org.example.backendai.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MedicalEncounterMapper {
    MedicalEncounter toMedicalEncounter(MedicalEncounterRequest request);

    MedicalEncounterResponse toMedicalEncounterResponse(MedicalEncounter medicalEncounter);
}

