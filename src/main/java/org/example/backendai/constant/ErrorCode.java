package org.example.backendai.constant;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,  makeFinal = true)
public enum ErrorCode {
    LOGIN_FAIL(1001, "Login fail", HttpStatus.BAD_REQUEST),
    USERNAME_EXISTED(1002, "Username already exists", HttpStatus.BAD_REQUEST),
    DOCTOR_NOT_EXISTED(1002, "Doctor already not exists", HttpStatus.BAD_REQUEST),
    USERNAME_NOT_EXISTED(1002, "Username already not exists", HttpStatus.BAD_REQUEST),
    PATIENT_NOT_EXISTED(1002, "Patient already not exists", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(1005, "Invalid password", HttpStatus.UNAUTHORIZED),

    // Message errors (3001-3020)
    USER_NOT_FOUND(3001, "User not found",HttpStatus.BAD_REQUEST),
    MESSAGE_NOT_FOUND(3001, "Message not found",HttpStatus.BAD_REQUEST),
    MESSAGE_SEND_FAILED(3002, "Failed to send message",HttpStatus.BAD_REQUEST),
    INVALID_MESSAGE_TYPE(3003, "Invalid message type",HttpStatus.BAD_REQUEST),
    ROOM_NOT_FOUND(3004, "Chat room not found",HttpStatus.BAD_REQUEST),

    // Classification errors (3021-3040)
    CLASSIFICATION_NOT_FOUND(3021, "Classification not found",HttpStatus.BAD_REQUEST),
    CLASSIFICATION_FAILED(3022, "AI classification failed",HttpStatus.BAD_REQUEST),
    ALREADY_VERIFIED(3023, "Classification already verified",HttpStatus.BAD_REQUEST),
    INVALID_URGENCY_LEVEL(3024, "Invalid urgency level",HttpStatus.BAD_REQUEST),

    // Permission errors (3041-3060)
    NOT_DOCTOR_ROLE(3041, "Only doctors can verify classifications",HttpStatus.BAD_REQUEST),
    NOT_MESSAGE_SENDER(3042, "You are not the sender of this message",HttpStatus.BAD_REQUEST),
    NOT_MESSAGE_RECEIVER(3043, "You are not the receiver of this message",HttpStatus.BAD_REQUEST),

    PATIENT_HAS_NO_MANAGING_DOCTOR(1010, "This patient has no managing doctor",HttpStatus.BAD_REQUEST),
    NO_DOCTORS_IN_DEPARTMENT(1011, "No doctors found in this department",HttpStatus.BAD_REQUEST);
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
