package com.haeil.full.cases.dto.response;

import com.haeil.full.cases.domain.Cases;
import com.haeil.full.cases.domain.type.CaseStatus;
import com.haeil.full.cases.domain.type.CaseType;
import java.time.LocalDateTime;
import java.util.List;

public record CompletedCaseDetailResponse(
        // 사건정보
        Long caseId,
        String caseNumber,
        String title,
        String content,
        CaseType caseType,
        CaseStatus caseStatus,
        LocalDateTime occurredDate,
        String attorneyName,
        // 사건진행결과
        List<CaseEventResponse> caseProgress) {
    public static CompletedCaseDetailResponse from(Cases cases) {
        return new CompletedCaseDetailResponse(
                cases.getId(),
                cases.getCaseNumber(),
                cases.getTitle(),
                cases.getContent(),
                cases.getCaseType(),
                cases.getCaseStatus(),
                cases.getOccurredDate(),
                cases.getAttorney() != null ? cases.getAttorney().getName() : null,
                cases.getCaseEventList().stream().map(CaseEventResponse::from).toList());
    }
}
