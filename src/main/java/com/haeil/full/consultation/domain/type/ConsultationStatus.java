package com.haeil.full.consultation.domain.type;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ConsultationStatus {
    IN_PROGRESS("상담 진행"),
    COMPLETED("상담 완료");

    private final String label;

    public String getLabel() {
        return label;
    }
}
