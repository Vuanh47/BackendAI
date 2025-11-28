package org.example.backendai.repository;

import org.example.backendai.entity.MessageClassification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MessageClassificationRepository extends JpaRepository<MessageClassification, Integer> {

    Optional<MessageClassification> findByPatientId(Integer patientId);
    boolean existsByPatientId(Integer patientId);

    @Query("SELECT mc FROM MessageClassification mc WHERE " +
            "mc.patient.id = :patientId AND mc.doctor.id = :doctorId")
    Optional<MessageClassification> findByPatientIdAndDoctorId(
            @Param("patientId") Integer patientId,
            @Param("doctorId") Integer doctorId);
}