package com.haeil.be.consultation.domain.type;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Status {

    private final String label;

    public String getLabel() {
        return label;
    }
}
