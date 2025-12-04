package com.haeil.full.cases.dto.response;

import com.haeil.full.cases.domain.Cases;
import com.haeil.full.cases.domain.type.CaseStatus;
import com.haeil.full.cases.domain.type.CaseType;
import java.time.LocalDateTime;

public record RequestedCaseResponse(
        Long caseId,
        String clientName,
        String title,
        CaseType caseType,
        String attorneyName,
        CaseStatus caseStatus,
        LocalDateTime occurredDate,
        LocalDateTime createdDate) {
    public static RequestedCaseResponse from(Cases cases) {
        return new RequestedCaseResponse(
                cases.getId(),
                cases.getClient() != null ? cases.getClient().getName() : "미지정",
                cases.getTitle(),
                cases.getCaseType(),
                cases.getAttorney() != null ? cases.getAttorney().getName() : "-",
                cases.getCaseStatus(),
                cases.getOccurredDate(),
                cases.getCreatedDate());
    }
}
