package com.haeil.full.cases.dto.response;

import com.haeil.full.cases.domain.Cases;
import com.haeil.full.cases.domain.type.CaseStatus;
import com.haeil.full.cases.domain.type.CaseType;
import java.time.LocalDateTime;

public record OngoingCaseResponse(
        Long caseId,
        String clientName,
        String title,
        CaseType caseType,
        CaseStatus caseStatus,
        LocalDateTime occurredDate,
        LocalDateTime createdDate,
        String attorneyName) {
    public static OngoingCaseResponse from(Cases cases) {
        return new OngoingCaseResponse(
                cases.getId(),
                cases.getClient() != null ? cases.getClient().getName() : "미지정",
                cases.getTitle(),
                cases.getCaseType(),
                cases.getCaseStatus(),
                cases.getOccurredDate(),
                cases.getCreatedDate(),
                cases.getAttorney() != null ? cases.getAttorney().getName() : null);
    }
}
