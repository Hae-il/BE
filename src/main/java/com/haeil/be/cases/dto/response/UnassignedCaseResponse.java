package com.haeil.be.cases.dto.response;

import com.haeil.be.cases.domain.Cases;
import com.haeil.be.cases.domain.type.CaseStatus;
import com.haeil.be.cases.domain.type.CaseType;
import java.time.LocalDateTime;

public record UnassignedCaseResponse(
        Long caseId,
        String clientName,
        String title,
        CaseType caseType,
        CaseStatus caseStatus,
        LocalDateTime occurredDate) {
    public static UnassignedCaseResponse from(Cases cases) {
        return new UnassignedCaseResponse(
                cases.getId(),
                cases.getClient().getName(),
                cases.getTitle(),
                cases.getCaseType(),
                cases.getCaseStatus(),
                cases.getOccurredDate());
    }
}
