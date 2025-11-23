package org.example.backendai.repository;

import org.example.backendai.entity.Doctor;
import org.example.backendai.entity.MedicalEncounter;
import org.example.backendai.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicalEncounterRepository extends JpaRepository<MedicalEncounter,Long> {
    // Tìm theo ID của MedicalEncounter (thường là findById)
    List<MedicalEncounter> findAllById(Integer id);

}
