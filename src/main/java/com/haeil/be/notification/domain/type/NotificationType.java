package com.haeil.be.notification.domain.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NotificationType {
    CASE_ASSIGNED("사건 배정 요청"),
    CASE_ASSIGNMENT_APPROVED("사건 배정 승인"),
    CASE_ASSIGNMENT_REJECTED("사건 배정 거절");

    private final String description;
}

