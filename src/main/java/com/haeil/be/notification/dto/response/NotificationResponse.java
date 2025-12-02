package com.haeil.be.notification.dto.response;

import com.haeil.be.notification.domain.Notification;
import com.haeil.be.notification.domain.type.NotificationType;
import java.time.LocalDateTime;

public record NotificationResponse(
        Long id,
        String content,
        String url,
        boolean isRead,
        NotificationType type,
        LocalDateTime createdAt) {
    public static NotificationResponse from(Notification notification) {
        return new NotificationResponse(
                notification.getId(),
                notification.getContent(),
                notification.getRelatedUrl(),
                notification.isRead(),
                notification.getNotificationType(),
                notification.getCreatedDate());
    }
}
