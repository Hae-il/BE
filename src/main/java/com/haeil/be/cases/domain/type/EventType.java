package com.haeil.be.cases.domain.type;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum EventType {
    // 여기는 맞게 더 채워주세요.
    HEARING_DATE("변론기일"),
    TRIAL_DATE("공판일"),
    MEDIATION_DATE("조정기일"),
    ;

    private final String label;

    public String getLabel() {
        return label;
    }
}
