package com.haeil.be.contract.domain.type;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ContractStatus {
    AWAITING("수임대기"),
    PENDING("입금대기"),
    COMPLETED("입금완료");

    private final String label;

    public String getLabel() {
        return label;
    }
}
