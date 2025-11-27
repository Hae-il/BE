package com.haeil.full.cases.dto.response;

import com.haeil.full.cases.domain.Cases;
import com.haeil.full.cases.domain.type.CaseStatus;
import com.haeil.full.cases.domain.type.CaseType;
import java.time.LocalDateTime;

public record RequestedCaseDetailResponse(
        Long caseId,
        String title,
        String content,
        CaseType caseType,
        CaseStatus caseStatus,
        LocalDateTime occurredDate,
        LocalDateTime modifiedDate,
        String opponentName,
        String opponentPhone,
        String opponentInsurance,
        Long attorneyId,
        String attorneyName,
        String attorneyEmail) {
    public static RequestedCaseDetailResponse from(Cases cases) {
        return new RequestedCaseDetailResponse(
                cases.getId(),
                cases.getTitle(),
                cases.getContent(),
                cases.getCaseType(),
                cases.getCaseStatus(),
                cases.getOccurredDate(),
                cases.getModifiedDate(),
                cases.getOpponentName(),
                cases.getOpponentPhone(),
                cases.getOpponentInsurance(),
                cases.getAttorney() != null ? cases.getAttorney().getId() : null,
                cases.getAttorney() != null ? cases.getAttorney().getName() : "미배정",
                cases.getAttorney() != null ? cases.getAttorney().getEmail() : "-");
    }
}
