package org.example.backendai.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,  makeFinal = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponse {
    String id;
    String fullName;
    String phone;
    LocalDate dateOfBirth;
    String gender;

}
