package com.haeil.full.notification.domain.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NotificationType {
    CASE_ASSIGNED("사건 배정 요청"),
    CASE_ASSIGNMENT_APPROVED("사건 배정 승인"),
    CASE_ASSIGNMENT_REJECTED("사건 배정 거절"),
    CASE_CREATED("사건 생성(미배정)"),
    CASE_COMPLETED("사건 완료");

    private final String description;
}
