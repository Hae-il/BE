package com.haeil.be.contract.domain.type;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ContractRequestStatus {
    REQUESTED("수임요청");

    private final String label;

    public String getLabel() {
        return label;
    }
}
