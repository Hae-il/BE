package com.haeil.be.consultation.domain.type;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ConsultationStatus {
    PENDING("예약대기"),
    REJECTED("예약거절"),
    APPROVED("예약승인"),

    WAITING("상담대기"),
    IN_PROGRESS("상담진행중"),
    COMPLETED("상담종료");

    private final String label;

    public String getLabel() {
        return label;
    }
}
