package org.example.backendai.constant;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,  makeFinal = true)

public enum SuccessCode {
    // --- CREATE (1000 - 1009) ---
    DEVICE_CREATED(1001, "Device created successfully", HttpStatus.CREATED),
    PATIENT_CREATED(1002, "Patient created successfully", HttpStatus.CREATED),
    DOCTOR_CREATED(1003, "Doctor created successfully", HttpStatus.CREATED),
    MEDICAL_ENCOUNTER_CREATED(1004, "MedicalEncounter created successfully", HttpStatus.CREATED),

    // --- LIST (1010 - 1019) ---
    MEDICAL_ENCOUNTER_LISTED(1010, "MedicalEncounter Device listed successfully", HttpStatus.OK),
    PATIENT_LISTED(1011, "Patient Device listed successfully", HttpStatus.OK),
    DOCTOR_LISTED(1012, "Doctor Device listed successfully", HttpStatus.OK),

    // --- GET (1020 - 1029) ---
    PATIENT_GET(1020, "Patient get successfully", HttpStatus.OK),
    DOCTOR_GET(1021, "Doctor get successfully", HttpStatus.OK),

    // --- UPDATE (1030 - 1039) ---
    // Thêm các mã cập nhật thành công
    PATIENT_UPDATED(1030, "Patient updated successfully", HttpStatus.OK),
    PASSWORD_UPDATED(1030, "Password updated successfully", HttpStatus.OK),
    DOCTOR_UPDATED(1031, "Doctor updated successfully", HttpStatus.OK),
    DEVICE_UPDATED(1032, "Device updated successfully", HttpStatus.OK),

    // --- AUTH & ANALYSIS (1040 - 1049) ---
    LOGIN_SUCCESS(1040, "Login successfully", HttpStatus.OK),
    ANALYZE_SUCCESS(1041, "Image analysis completed successfully", HttpStatus.OK),

    // --- Message codes (2001-2020) ---
    SEND_MESSAGE_SUCCESS(2001, "Message sent successfully", HttpStatus.OK),
    GET_MESSAGES_SUCCESS(2002, "Messages retrieved successfully", HttpStatus.OK),
    DELETE_MESSAGE_SUCCESS(2003, "Message deleted successfully", HttpStatus.OK),
    DELETE_MESSAGES_SUCCESS(2004, "Messages deleted successfully", HttpStatus.OK),
    USER_JOINED_SUCCESS(2005, "User joined chat room successfully", HttpStatus.OK),
    USER_LEFT_SUCCESS(2006, "User left chat room successfully", HttpStatus.OK),
    MESSAGE_MARKED_READ(2007, "Message marked as read successfully", HttpStatus.OK),
    ALL_MESSAGES_MARKED_READ(2008, "All messages marked as read successfully", HttpStatus.OK),
    GET_ROOMS_SUCCESS(2009, "Rooms retrieved successfully", HttpStatus.OK),
    GET_UNREAD_COUNT_SUCCESS(2010, "Unread count retrieved successfully", HttpStatus.OK),

    CLASSIFICATION_RETRIEVED(1022, "Lấy thông tin phân loại thành công", HttpStatus.OK),
    MESSAGE_CLASSIFIED(1021, "Phân loại tin nhắn thành công", HttpStatus.OK);

    int code;
    String message;
    HttpStatus status;

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }
}