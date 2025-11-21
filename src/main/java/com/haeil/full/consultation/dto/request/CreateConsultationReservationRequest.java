package com.haeil.full.consultation.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.haeil.full.cases.domain.type.CaseType;
import com.haeil.full.consultation.domain.ConsultationReservation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@NoArgsConstructor
public class CreateConsultationReservationRequest {

    @NotBlank(message = "이름은 필수 입력 항목입니다.")
    private String name;

    @NotBlank(message = "전화번호는 필수 입력 항목입니다.")
    @Pattern(regexp = "^01[0-9]-\\d{4}-\\d{4}$", message = "전화번호 형식이 올바르지 않습니다.")
    private String phone;

    @NotNull(message = "사건 유형은 필수 입력 항목입니다.")
    private CaseType caseType;

    private String description;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime requestedDate;

    public ConsultationReservation toEntity() {
        return ConsultationReservation.builder()
                .name(this.name)
                .phone(this.phone)
                .requestedDate(this.requestedDate)
                .caseType(this.caseType)
                .description(this.description)
                .build();
    }
}
