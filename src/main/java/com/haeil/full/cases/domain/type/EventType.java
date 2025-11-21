package com.haeil.full.cases.domain.type;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum EventType {
    HEARING_DATE("변론기일"),
    TRIAL_DATE("공판일"),
    MEDIATION_DATE("조정기일"),
    CONFERENCE_DATE("준비기일"),
    SENTENCE_DATE("선고기일"),
    INVESTIGATION_DATE("조사기일");

    private final String label;

    public String getLabel() {
        return label;
    }
}
