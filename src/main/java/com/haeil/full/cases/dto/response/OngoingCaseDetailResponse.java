package com.haeil.full.cases.dto.response;

import com.haeil.full.cases.domain.Cases;
import com.haeil.full.cases.domain.type.CaseStatus;
import com.haeil.full.cases.domain.type.CaseType;
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
        LocalDateTime modifiedDate,
        String opponentName,
        String opponentPhone,
        String opponentInsurance,
        String attorneyName,
        String attorneyEmail,
        PetitionResponse petition,
        // 사건진행결과
        List<CaseEventResponse> caseProgress,
        // 소송문서
        List<CaseDocumentResponse> documents) {
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
                cases.getModifiedDate(),
                cases.getOpponentName(),
                cases.getOpponentPhone(),
                cases.getOpponentInsurance(),
                cases.getAttorney() != null ? cases.getAttorney().getName() : null,
                cases.getAttorney() != null ? cases.getAttorney().getEmail() : null,
                cases.getPetition() != null ? PetitionResponse.from(cases.getPetition()) : null,
                cases.getCaseEventList().stream().map(CaseEventResponse::from).toList(),
                cases.getCaseDocumentList().stream().map(CaseDocumentResponse::from).toList());
    }
}
