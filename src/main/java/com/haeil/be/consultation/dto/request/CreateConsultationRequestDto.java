package com.haeil.be.consultation.dto.request;

import com.haeil.be.cases.domain.type.CaseType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateConsultationRequestDto {

    @NotBlank(message = "이름은 필수 입력 항목입니다.")
    private String name;

    @NotBlank(message = "전화번호는 필수 입력 항목입니다.")
    private String phone;

    private LocalDateTime preferredDatetime;

    @NotNull(message = "사건 유형은 필수 입력 항목입니다.")
    private CaseType caseType;

    private String briefDescription;
}
