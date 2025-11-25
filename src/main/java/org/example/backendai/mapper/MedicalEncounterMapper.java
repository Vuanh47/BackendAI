// File: org.example.backendai.mapper.MedicalEncounterMapper.java (Sửa chữa)

package org.example.backendai.mapper;

import org.example.backendai.dto.request.MedicalEncounterRequest;
import org.example.backendai.dto.response.MedicalEncounterResponse;
import org.example.backendai.entity.MedicalEncounter;
// ... các import khác ...
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings; // Thêm import này

@Mapper(componentModel = "spring")
public interface MedicalEncounterMapper {

    // Ánh xạ từ Request sang Entity
    // Bạn cần ánh xạ ngược lại các trường nếu có sự khác biệt về tên

    @Mapping(target = "id", ignore = true) // ID sẽ được DB sinh ra
    @Mapping(target = "patient", ignore = true) // Patient được gán thủ công trong Service
    @Mapping(target = "BHYT", source = "BHYT")
    MedicalEncounter toMedicalEncounter(MedicalEncounterRequest request);


    @Mappings({
            @Mapping(source = "medicalEncounter.id", target = "encounterId"),
            @Mapping(source = "medicalEncounter.patient.id", target = "patientId"),
            @Mapping(
                    target = "patientName",
                    expression = "java(medicalEncounter.getPatient() != null && medicalEncounter.getPatient().getUser() != null ? medicalEncounter.getPatient().getUser().getFullName() : null)"
            )
    })
    MedicalEncounterResponse toMedicalEncounterResponse(MedicalEncounter medicalEncounter);
}