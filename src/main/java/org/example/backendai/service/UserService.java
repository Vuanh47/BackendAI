package org.example.backendai.service;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.example.backendai.constant.ErrorCode;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {


}
