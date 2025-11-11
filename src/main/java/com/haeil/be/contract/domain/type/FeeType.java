package com.haeil.be.contract.domain.type;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum FeeType {
    FIXED("정액"),
    PERCENTAGE("정률");

    private final String label;

    public String getLabel() {
        return label;
    }
}
