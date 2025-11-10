package com.haeil.be.consultation.dto.request;

import com.haeil.be.consultation.domain.type.ConsultationRequestStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateConsultationRequestStatusDto {

    @NotNull(message = "상태는 필수 입력 항목입니다.")
    private ConsultationRequestStatus status;

    private Long assignedLawyerId;

    private String rejectReason;
}
