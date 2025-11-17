package com.haeil.be.cases.dto.response;

public record CaseManagementResponse(
        Boolean canDraftComplaint, Boolean canUploadDocument, Boolean canWriteClosingReport) {
    public static CaseManagementResponse createDefault() {
        // TODO: 나중에 구현될 로직에 따라 결정
        // 현재는 기본값으로 true 반환
        return new CaseManagementResponse(true, true, true);
    }
}
