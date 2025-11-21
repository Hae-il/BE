package com.haeil.full.contract.domain.type;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ExpenseHandling {
    BILLED_SEPARATELY("별도계산"),
    INCLUDED_IN_RETAINER("착수금에 포함");

    private final String label;

    public String getLabel() {
        return label;
    }
}
