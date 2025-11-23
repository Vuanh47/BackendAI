package org.example.backendai.service;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.example.backendai.constant.ErrorCode;
import org.example.backendai.constant.UserRole;
import org.example.backendai.dto.request.DoctorRegisterRequest;
import org.example.backendai.dto.request.DoctorUpdateRequest;
import org.example.backendai.dto.response.DoctorResponse;
import org.example.backendai.entity.Doctor;
import org.example.backendai.entity.User;
import org.example.backendai.exception.AppException;
import org.example.backendai.mapper.DoctorMapper;
import org.example.backendai.repository.DoctorRepository;
import org.example.backendai.repository.PatientRepository;
import org.example.backendai.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DoctorService {
     UserRepository userRepository;
     DoctorRepository doctorRepository;
     DoctorMapper mapper;
     PasswordEncoder passwordEncoder;
     PatientRepository patientRepository;

    public DoctorResponse registerDoctor(DoctorRegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AppException(ErrorCode.USERNAME_EXISTED);
        }

        User user = mapper.toUser(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(UserRole.DOCTOR);

        Doctor doctor = mapper.toDoctor(request);
        doctor.setUser(user);
        doctor.setDepartment(request.getDepartment());
        doctor.setSpecialty(request.getSpecialty());
        userRepository.save(user);
        doctorRepository.save(doctor);

        return mapper.toDoctorResponse(doctor, user);
    }

    public List<DoctorResponse> getAllDoctors() {
        List<Doctor> doctors = doctorRepository.findAll();

        if (doctors.isEmpty()) {
            throw new AppException(ErrorCode.DOCTOR_NOT_EXISTED);
        }

        return doctors.stream()
                .map(doctor -> mapper.toDoctorResponse(doctor, doctor.getUser()))
                .toList();
    }

    public DoctorResponse getDoctorById(Integer id) {
         Doctor doctor = doctorRepository.findById(Long.valueOf(id)).orElseThrow(() ->
                new AppException(ErrorCode.DOCTOR_NOT_EXISTED));

        return mapper.toDoctorResponse(doctor, doctor.getUser());
    }// Giả định: File DoctorService.java (Sửa hàm updateDoctor)

    public DoctorResponse updateDoctor(Long id, DoctorUpdateRequest request) {
        Doctor existingDoctor = doctorRepository.findById(id).orElseThrow(() ->
                new AppException(ErrorCode.DOCTOR_NOT_EXISTED));
        User existingUser = existingDoctor.getUser();
        mapper.updateUserFromRequest(request, existingUser);

        userRepository.save(existingUser);


        return mapper.toDoctorResponse(existingDoctor, existingUser);
    }
}
