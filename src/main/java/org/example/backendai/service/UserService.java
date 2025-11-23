package org.example.backendai.service;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.example.backendai.constant.ErrorCode;
import org.example.backendai.dto.request.DoctorUpdateRequest;
import org.example.backendai.dto.request.PasswordUpdateRequest;
import org.example.backendai.dto.request.PatientRegisterRequest;
import org.example.backendai.dto.request.UserUpdateRequest;
import org.example.backendai.dto.response.DoctorResponse;
import org.example.backendai.dto.response.PatientResponse;
import org.example.backendai.dto.response.UserResponse;
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

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    UserMapper userMapper;
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    public UserResponse updateDoctor(Long id, UserUpdateRequest request) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new AppException(ErrorCode.DOCTOR_NOT_EXISTED));
        userMapper.updateUserFromRequest(request, user);
        userRepository.save(user);
        return userMapper.toUserResponse(user);
    }

    public void updatePassword(Long userId, PasswordUpdateRequest request) {

        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USERNAME_NOT_EXISTED)); // Giả định có mã lỗi này

        String newPassword = request.getPassword();
        if (newPassword == null || newPassword.trim().isEmpty()) {
            throw new AppException(ErrorCode.INVALID_PASSWORD); // Hoặc mã lỗi tương tự
        }


        String encodedPassword = passwordEncoder.encode(newPassword);
        existingUser.setPassword(encodedPassword);

        userRepository.save(existingUser);
    }
}
