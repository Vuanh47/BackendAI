package org.example.backendai.repository;

import org.example.backendai.constant.SeverityLevel;
import org.example.backendai.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PatientRepository extends JpaRepository<Patient,Long> {
    List<Patient> findAllByManagingDoctor_Id(Integer doctorId);


    @Query("SELECT p FROM Patient p " +
            "WHERE p.managingDoctor.id = :doctorId " +
            "AND EXISTS (" +
            "  SELECT 1 FROM MessageClassification mc " +
            "  WHERE mc.patient.id = p.id " +
            "  AND mc.doctor.id = :doctorId " +
            "  AND mc.AIClassification = :severity" +
            ")")
    List<Patient> findPatientsByDoctorAndSeverity(
            @Param("doctorId") Integer doctorId,
            @Param("severity") SeverityLevel severity);
}