package org.example.backendai.mapper;

import org.example.backendai.dto.request.DoctorUpdateRequest;
import org.example.backendai.dto.response.DoctorResponse;
import org.example.backendai.entity.Doctor;
import org.example.backendai.dto.request.DoctorRegisterRequest; // Giả định có request này
import org.example.backendai.entity.User; // Giả định cần ánh xạ User
import org.mapstruct.Mapper;
import org.mapstruct.Mapping; // Thêm import này
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;


@Mapper(componentModel = "spring")
public interface DoctorMapper {

    // Ánh xạ Doctor và User sang DoctorResponse
    @Mappings({
            // BẮT BUỘC: Chỉ định nguồn cho thuộc tính "id" của DoctorResponse
            @Mapping(source = "doctor.user.id", target = "id"),

            // Ánh xạ các thuộc tính từ User sang DoctorResponse
            @Mapping(source = "doctor.user.fullName", target = "fullName"),
            @Mapping(source = "doctor.user.phone", target = "phone"),
            @Mapping(source = "doctor.user.dateOfBirth", target = "dateOfBirth"),
            @Mapping(source = "doctor.user.gender", target = "gender"),

            // Ánh xạ các thuộc tính từ Doctor sang DoctorResponse
            @Mapping(source = "doctor.specialty", target = "specialty"),
            @Mapping(source = "doctor.department", target = "department")
    })
    DoctorResponse toDoctorResponse(Doctor doctor);

    // Lưu ý: Nếu phương thức của bạn có 2 đối số (Doctor và User)
    // DoctorResponse toDoctorResponse(Doctor doctor, User user);
    // Thì bạn sẽ phải chỉnh sửa source tương ứng (ví dụ: source = "user.id" thay vì "doctor.user.id").
    // Tuy nhiên, dựa trên Service của bạn, phương thức có thể trông như sau:
    @Mappings({
            @Mapping(source = "user.id", target = "id"),
            @Mapping(source = "user.fullName", target = "fullName"),
            @Mapping(source = "user.phone", target = "phone"),
            @Mapping(source = "user.dateOfBirth", target = "dateOfBirth"),
            @Mapping(source = "user.gender", target = "gender"),
            @Mapping(source = "doctor.specialty", target = "specialty"),
            @Mapping(source = "doctor.department", target = "department")
    })
    DoctorResponse toDoctorResponse(Doctor doctor, User user);

    // Hoàn thành các phương thức khác nếu cần (ví dụ: ánh xạ từ Request)

    // Ánh xạ DoctorRegisterRequest sang User Entity
    User toUser(DoctorRegisterRequest request);

    // Ánh xạ DoctorRegisterRequest sang Doctor Entity
    Doctor toDoctor(DoctorRegisterRequest request);

    // Phương thức ánh xạ từ Request sang Doctor Entity để cập nhật
    @Mapping(target = "id", ignore = true) // Luôn bỏ qua ID khi cập nhật
    @Mapping(target = "user", ignore = true)
    void updateDoctorFromRequest(DoctorUpdateRequest request, @MappingTarget Doctor doctor);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "username", ignore = true),
            @Mapping(target = "password", ignore = true),
            @Mapping(target = "role", ignore = true)

    })
    void updateUserFromRequest(DoctorUpdateRequest request, @MappingTarget User user);
}