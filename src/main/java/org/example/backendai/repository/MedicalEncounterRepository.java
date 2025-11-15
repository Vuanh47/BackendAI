package org.example.backendai.repository;

import org.example.backendai.entity.Doctor;
import org.example.backendai.entity.MedicalEncounter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicalEncounterRepository extends JpaRepository<MedicalEncounter,Long> {


}
