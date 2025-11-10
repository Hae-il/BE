package com.haeil.be.consultation.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateConsultationDto {

    @NotNull(message = "상담 예약 ID는 필수 입력 항목입니다.")
    private Long consultationRequestId;

    @Valid
    @NotNull(message = "의뢰인 정보는 필수 입력 항목입니다.")
    private ClientInfoDto clientInfo;

    private LocalDateTime consultationDate;

    private String location;

    @Getter
    @NoArgsConstructor
    public static class ClientInfoDto {
        private String name;
        private String email;
        private String phone;
        private String address;
        private String residentNumber;
        private java.time.LocalDate birthDate;
        private String gender;
        private String jobTitle;
    }
}
