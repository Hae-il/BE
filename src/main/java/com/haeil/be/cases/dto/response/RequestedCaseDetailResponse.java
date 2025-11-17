package com.haeil.be.cases.dto.response;

import com.haeil.be.cases.domain.Cases;
import com.haeil.be.cases.domain.type.CaseStatus;
import com.haeil.be.cases.domain.type.CaseType;
import java.time.LocalDateTime;

public record RequestedCaseDetailResponse(
        Long caseId,
        String title,
        String content,
        CaseType caseType,
        CaseStatus caseStatus,
        LocalDateTime occurredDate) {
    public static RequestedCaseDetailResponse from(Cases cases) {
        return new RequestedCaseDetailResponse(
                cases.getId(),
                cases.getTitle(),
                cases.getContent(),
                cases.getCaseType(),
                cases.getCaseStatus(),
                cases.getOccurredDate());
    }
}
