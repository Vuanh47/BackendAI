package org.example.backendai.service;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.example.backendai.constant.ErrorCode;
import org.example.backendai.constant.UserRole;
import org.example.backendai.dto.request.PatientRegisterRequest;
import org.example.backendai.dto.response.PatientResponse;
import org.example.backendai.entity.Doctor;
import org.example.backendai.entity.Patient;
import org.example.backendai.entity.User;
import org.example.backendai.exception.AppException;
import org.example.backendai.mapper.DoctorMapper;
import org.example.backendai.mapper.PatientMapper;
import org.example.backendai.mapper.UserMapper;
import org.example.backendai.repository.DoctorRepository;
import org.example.backendai.repository.PatientRepository;
import org.example.backendai.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PatientService {
    PasswordEncoder passwordEncoder;
    UserMapper userMapper;
    UserRepository userRepository;
    PatientRepository repository;
    PatientMapper mapper;
    DoctorRepository doctorRepository;

    private final DoctorMapper doctorMapper;

    public PatientResponse registerPatient(PatientRegisterRequest request){
        if (userRepository.existsByUsername(request.getUsername())){
            throw new AppException(ErrorCode.USERNAME_EXISTED);
        }
        Doctor doctor = doctorRepository.findById(request.getManagingDoctorId().longValue())
                .orElseThrow(() -> new AppException(ErrorCode.DOCTOR_NOT_EXISTED));
        User user = userMapper.toUser(request);
        user.setPhone(request.getPhone());
        user.setRole(UserRole.PATIENT);
        user.setPassword(passwordEncoder.encode(user.getPassword()));


        Patient patient = mapper.toPatient(request);
        patient.setManagingDoctor(doctor);
        patient.setRelativePhoneNumber(request.getRelativePhoneNumber());
        patient.setUser(user);

        userRepository.save(user);
        repository.save(patient);

        return mapper.toPatientResponse(patient, user);
    }

    public List<PatientResponse> getAllPatient(){
        List<Patient> patients = repository.findAll();
        if (patients.isEmpty()){
            throw new AppException(ErrorCode.PATIENT_NOT_EXISTED);
        }

        return patients.stream()
                .map(patient ->
                        mapper.toPatientResponse(patient, patient.getUser())).toList();

    }
}
