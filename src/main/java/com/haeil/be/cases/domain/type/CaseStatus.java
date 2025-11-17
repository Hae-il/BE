package com.haeil.be.cases.domain.type;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum CaseStatus {
    UNASSIGNED("미배정"),
    PENDING("배정요청"),
    IN_PROGRESS("진행중"),
    COMPLETED("완료");

    private final String label;

    public String getLabel() {
        return label;
    }
}
