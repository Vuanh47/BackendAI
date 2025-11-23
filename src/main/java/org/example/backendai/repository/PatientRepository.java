package org.example.backendai.repository;

import org.example.backendai.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PatientRepository extends JpaRepository<Patient,Long> {
    List<Patient> findAllByManagingDoctor_Id(Integer doctorId);
}