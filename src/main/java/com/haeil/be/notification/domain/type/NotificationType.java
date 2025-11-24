package com.haeil.be.notification.domain.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NotificationType {
    CASE_ASSIGNED("사건 배정 요청");

    private final String description;
}

