package org.example.backendai.repository;

import org.example.backendai.entity.Doctor;
import org.example.backendai.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorRepository extends JpaRepository<Doctor,Long> {


}
