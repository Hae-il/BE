package com.haeil.full.cases.dto.response;

import com.haeil.full.cases.domain.Cases;
import com.haeil.full.cases.domain.type.CaseStatus;
import com.haeil.full.cases.domain.type.CaseType;
import java.time.LocalDateTime;

public record RequestedCaseResponse(
        Long caseId,
        String title,
        CaseType caseType,
        CaseStatus caseStatus,
        LocalDateTime occurredDate) {
    public static RequestedCaseResponse from(Cases cases) {
        return new RequestedCaseResponse(
                cases.getId(),
                cases.getTitle(),
                cases.getCaseType(),
                cases.getCaseStatus(),
                cases.getOccurredDate());
    }
}
