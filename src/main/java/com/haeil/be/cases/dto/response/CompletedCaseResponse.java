package com.haeil.be.cases.dto.response;

import com.haeil.be.cases.domain.Cases;
import com.haeil.be.cases.domain.type.CaseStatus;
import com.haeil.be.cases.domain.type.CaseType;
import java.time.LocalDateTime;

public record CompletedCaseResponse(
        Long caseId,
        String title,
        CaseType caseType,
        CaseStatus caseStatus,
        LocalDateTime occurredDate,
        String attorneyName) {
    public static CompletedCaseResponse from(Cases cases) {
        return new CompletedCaseResponse(
                cases.getId(),
                cases.getTitle(),
                cases.getCaseType(),
                cases.getCaseStatus(),
                cases.getOccurredDate(),
                cases.getAttorney() != null ? cases.getAttorney().getName() : null);
    }
}

