package org.example.backendai.constant;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ErrorCode {

    // Authentication errors (1001–1020)
    LOGIN_FAIL(1001, "Login failed", HttpStatus.BAD_REQUEST),
    USERNAME_EXISTED(1002, "Username already exists", HttpStatus.BAD_REQUEST),
    DOCTOR_NOT_EXISTED(1003, "Doctor does not exist", HttpStatus.BAD_REQUEST),
    USERNAME_NOT_EXISTED(1004, "Username does not exist", HttpStatus.BAD_REQUEST),
    PATIENT_NOT_EXISTED(1005, "Patient does not exist", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(1006, "Invalid password", HttpStatus.UNAUTHORIZED),

    PATIENT_HAS_NO_MANAGING_DOCTOR(1010, "This patient has no managing doctor", HttpStatus.BAD_REQUEST),
    NO_DOCTORS_IN_DEPARTMENT(1011, "No doctors found in this department", HttpStatus.BAD_REQUEST),

    // Message errors (3001–3020)
    USER_NOT_FOUND(3001, "User not found", HttpStatus.BAD_REQUEST),
    MESSAGE_NOT_FOUND(3002, "Message not found", HttpStatus.BAD_REQUEST),
    MESSAGE_SEND_FAILED(3003, "Failed to send message", HttpStatus.BAD_REQUEST),
    INVALID_MESSAGE_TYPE(3004, "Invalid message type", HttpStatus.BAD_REQUEST),
    ROOM_NOT_FOUND(3005, "Chat room not found", HttpStatus.BAD_REQUEST),

    // Classification errors (3021–3040)
    CLASSIFICATION_NOT_FOUND(3021, "Classification not found", HttpStatus.BAD_REQUEST),
    CLASSIFICATION_FAILED(3022, "AI classification failed", HttpStatus.BAD_REQUEST),
    ALREADY_VERIFIED(3023, "Classification already verified", HttpStatus.BAD_REQUEST),
    INVALID_URGENCY_LEVEL(3024, "Invalid urgency level", HttpStatus.BAD_REQUEST),

    // ⭐ NEW ERROR
    INVALID_SEVERITY_LEVEL(3025, "Invalid severity level", HttpStatus.BAD_REQUEST),
    NO_UNREAD_MESSAGES(4004, "Không có tin nhắn chưa đọc từ bệnh nhân này",  HttpStatus.BAD_REQUEST),
    // Permission errors (3041–3060)
    NOT_DOCTOR_ROLE(3041, "Only doctors can verify classifications", HttpStatus.BAD_REQUEST),
    NOT_MESSAGE_SENDER(3042, "You are not the sender of this message", HttpStatus.BAD_REQUEST),
    NOT_MESSAGE_RECEIVER(3043, "You are not the receiver of this message", HttpStatus.BAD_REQUEST),

    // Medical encounter errors
    MEDICAL_ENCOUNTER_NOT_EXISTED(4001, "Medical encounter not found", HttpStatus.NOT_FOUND),

    // AI API errors
    AI_API_ERROR(5001, "AI API request error", HttpStatus.INTERNAL_SERVER_ERROR),
    AI_API_NO_RESPONSE(5002, "AI API returned no response", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_AI_LABEL(5003, "Invalid AI label returned", HttpStatus.BAD_REQUEST),

    CLASSIFICATION_NOT_EXISTED(4004, "No classification available for this patient", HttpStatus.NOT_FOUND),

    MESSAGE_NOT_EXISTED(4005, "Message does not exist", HttpStatus.NOT_FOUND),
    MESSAGE_CLASSIFICATION_NOT_EXISTED(4005, "Message classification does not exist", HttpStatus.NOT_FOUND);

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
