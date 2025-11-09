package com.haeil.be.cases.domain.type;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum CaseType {
    CIVIL("민사"),
    CRIMINAL("형사"),
    TRAFFIC_ACCIDENT("교통사고"),
    OTHER("기타");

    private final String label;

    public String getLabel() {
        return label;
    }
}
