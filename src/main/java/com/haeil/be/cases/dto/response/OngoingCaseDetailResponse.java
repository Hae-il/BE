package com.haeil.be.cases.dto.response;

import com.haeil.be.cases.domain.Cases;
import com.haeil.be.cases.domain.type.CaseStatus;
import com.haeil.be.cases.domain.type.CaseType;
import java.time.LocalDateTime;
import java.util.List;

public record OngoingCaseDetailResponse(
        // 사건관리(소장, 소송문서, 종결보고서)
        CaseManagementResponse caseManagement,
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
        List<CaseEventResponse> caseProgress
) {
    public static OngoingCaseDetailResponse from(Cases cases) {
        return new OngoingCaseDetailResponse(
                CaseManagementResponse.createDefault(),
                cases.getId(),
                cases.getCaseNumber(),
                cases.getTitle(),
                cases.getContent(),
                cases.getCaseType(),
                cases.getCaseStatus(),
                cases.getOccurredDate(),
                cases.getAttorney() != null ? cases.getAttorney().getName() : null,
                cases.getCaseEventList().stream()
                        .map(CaseEventResponse::from)
                        .toList()
        );
    }
}

