package com.haeil.be.notification.controller;

import com.haeil.be.auth.exception.AuthException;
import com.haeil.be.auth.exception.errorcode.AuthErrorCode;
import com.haeil.be.notification.service.NotificationService;
import com.haeil.be.user.service.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Tag(name = "Notification", description = "알림 API")
@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @Operation(summary = "알림 구독", description = "SSE 연결을 통해 실시간 알림을 수신합니다.")
    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(@AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails == null) {
            throw new AuthException(AuthErrorCode.INVALID_TOKEN);
        }
        return notificationService.subscribe(userDetails.getId());
    }
}
