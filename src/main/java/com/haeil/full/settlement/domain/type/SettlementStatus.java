package com.haeil.full.settlement.domain.type;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum SettlementStatus {
    NONE("미작성"),
    DRAFT("작성중"),
    FINAL("작성완료");

    private final String label;

    public String getLabel() {
        return label;
    }
}
