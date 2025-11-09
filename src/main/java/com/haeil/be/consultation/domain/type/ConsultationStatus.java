package com.haeil.be.consultation.domain.type;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ConsultationStatus {
    CONSULTATION_WAITING("상담 대기"),
    CONSULTATION_IN_PROGRESS("상담 진행"),
    CONTRACT_PENDING("수임 대기"),
    CONTRACT_IN_PROGRESS("수임 진행"),
    COMPLETED("완료");

    private final String label;

    public String getLabel() {
        return label;
    }
}
