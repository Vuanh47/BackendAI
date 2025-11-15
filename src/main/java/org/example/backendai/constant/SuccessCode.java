package org.example.backendai.constant;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,  makeFinal = true)

public enum SuccessCode {
    DEVICE_CREATED(1000, "Device created successfully", HttpStatus.CREATED),
    PATIENT_CREATED(1000, "Patient created successfully", HttpStatus.CREATED),
    DOCTOR_CREATED(1000, "Doctor created successfully", HttpStatus.CREATED),
    MEDICAL_ENCOUNTER_CREATED(1000, "MedicalEncounter created successfully", HttpStatus.CREATED),

    MEDICAL_ENCOUNTER_LISTED(1000, "MedicalEncounter Device listed successfully", HttpStatus.OK),
    PATIENT_LISTED(1000, "Patient Device listed successfully", HttpStatus.OK),
    DOCTOR_LISTED(1000, "Doctor Device listed successfully", HttpStatus.OK),

    LOGIN_SUCCESS(1000, "Login successfully", HttpStatus.OK),
    ANALYZE_SUCCESS(1000, "Image analysis completed successfully", HttpStatus.OK),

    // Message codes (2001-2020)
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

    PATIENT_GET(1000, "Patient get successfully", HttpStatus.CREATED),
    DOCTOR_GET(1000, "Doctor get successfully", HttpStatus.CREATED);

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
