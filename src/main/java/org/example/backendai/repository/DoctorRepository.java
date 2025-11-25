package org.example.backendai.repository;

import org.example.backendai.constant.Department;
import org.example.backendai.entity.Doctor;
import org.example.backendai.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DoctorRepository extends JpaRepository<Doctor, Integer> {
    List<Doctor> findByDepartment(Department department);

    // Nếu cần include cả thông tin User (eager hoặc join fetch)
    @Query("SELECT d FROM Doctor d JOIN FETCH d.user WHERE d.department = :department")
    List<Doctor> findByDepartmentWithUser(@Param("department") Department department);
}