package com.haeil.be.settlement.domain.type;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PaymentStatus {
    // REQUEST("수임요청"),
    PENDING("임금대기"),
    COMPLETED("입금완료");

    private final String label;

    public String getLabel() {
        return label;
    }
}
