package com.haeil.full.cases.domain.type;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum DocumentType {
    ANSWER("답변서"),
    PREPARATORY_BRIEF("준비서면"),
    EVIDENCE("증거자료"),
    JUDGEMENT("판결문"),
    ETC("기타");

    private final String label;

    public String getLabel() {
        return label;
    }
}
