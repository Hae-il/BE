package com.haeil.full.consultation.domain.type;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ConsultationRequestStatus {
    PENDING("예약 대기"),
    REJECTED("예약 거절"),
    APPROVED("예약 승인");

    private final String label;

    public String getLabel() {
        return label;
    }
}
