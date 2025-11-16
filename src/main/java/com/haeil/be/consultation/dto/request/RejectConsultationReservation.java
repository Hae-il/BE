package com.haeil.be.consultation.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RejectConsultationReservation {

    @NotBlank(message = "거절 사유는 필수입니다.")
    private String rejectionReason;
}
