package com.haeil.full.cases.dto.response;

import com.haeil.full.cases.domain.Cases;

public record CaseInfoResponse(String attorneyName, String caseType, String clientName) {
    public static CaseInfoResponse from(Cases cases) {
        return new CaseInfoResponse(
                cases.getAttorney().getName(),
                cases.getCaseType().getLabel(),
                cases.getClient().getName());
    }
}
