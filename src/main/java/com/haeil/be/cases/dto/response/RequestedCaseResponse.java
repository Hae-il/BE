package com.haeil.be.cases.dto.response;

import com.haeil.be.cases.domain.Cases;
import com.haeil.be.cases.domain.type.CaseStatus;
import com.haeil.be.cases.domain.type.CaseType;
import java.time.LocalDateTime;

public record RequestedCaseResponse(
        Long caseId,
        String title,
        CaseType caseType,
        CaseStatus caseStatus,
        LocalDateTime occurredDate
) {
    public static RequestedCaseResponse from(Cases cases) {
        return new RequestedCaseResponse(
                cases.getId(),
                cases.getTitle(),
                cases.getCaseType(),
                cases.getCaseStatus(),
                cases.getOccurredDate()
        );
    }
}

