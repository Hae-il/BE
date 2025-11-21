package com.haeil.full.consultation.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ApproveConsultationReservation {

    @NotNull(message = "담당 변호사 ID는 필수입니다.")
    private Long lawyerId;
}
