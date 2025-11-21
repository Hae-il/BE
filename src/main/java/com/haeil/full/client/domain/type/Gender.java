package com.haeil.full.client.domain.type;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Gender {
    MALE("남성"),
    FEMALE("여성");

    private final String label;

    public String getLabel() {
        return label;
    }
}
