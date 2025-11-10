package com.haeil.be.consultation.dto.request;

import com.haeil.be.consultation.domain.type.ConsultationStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateConsultationStatusDto {

    @NotNull(message = "상담 상태는 필수 입력 항목입니다.")
    private ConsultationStatus status;
}
