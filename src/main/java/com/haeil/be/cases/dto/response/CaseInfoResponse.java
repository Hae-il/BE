package com.haeil.be.cases.dto.response;

import com.haeil.be.cases.domain.Cases;

public record CaseInfoResponse(String attorneyName, String caseType) {
    // String clientName 가 들어가야하는데 사건이랑 상담이 매핑이 안되어 있어서 수정되면 추가하겠습니다.
    public static CaseInfoResponse from(Cases cases) {
        return new CaseInfoResponse(
                cases.getAttorney().getName(),
                cases.getCaseType().getLabel()
        );
    }
}
