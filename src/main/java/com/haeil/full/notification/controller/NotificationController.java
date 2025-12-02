package com.haeil.full.notification.controller;

import com.haeil.full.auth.exception.AuthException;
import com.haeil.full.auth.exception.errorcode.AuthErrorCode;
import com.haeil.full.global.response.ApiResponse;
import com.haeil.full.notification.service.NotificationService;
import com.haeil.full.user.service.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @Operation(summary = "알림 목록 조회", description = "읽지 않은 알림 목록을 조회합니다.")
    @GetMapping
    public ApiResponse<Object> getNotifications(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails == null) {
            throw new AuthException(AuthErrorCode.INVALID_TOKEN);
        }
        return ApiResponse.from(notificationService.getNotifications(userDetails.getId()));
    }

    @Operation(summary = "알림 읽음 처리", description = "특정 알림을 읽음 상태로 변경합니다.")
    @PatchMapping("/{id}")
    public ApiResponse<Object> readNotification(
            @PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails == null) {
            throw new AuthException(AuthErrorCode.INVALID_TOKEN);
        }
        notificationService.readNotification(id, userDetails.getId());
        return ApiResponse.builder()
                .isSuccess(true)
                .code("REQUEST_OK")
                .message("알림을 읽음 처리했습니다.")
                .results(null)
                .build();
    }

    @Operation(summary = "모든 알림 읽음 처리", description = "모든 알림을 읽음 상태로 변경합니다.")
    @PatchMapping("/read-all")
    public ApiResponse<Object> readAllNotifications(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails == null) {
            throw new AuthException(AuthErrorCode.INVALID_TOKEN);
        }
        notificationService.readAllNotifications(userDetails.getId());
        return ApiResponse.builder()
                .isSuccess(true)
                .code("REQUEST_OK")
                .message("모든 알림을 읽음 처리했습니다.")
                .results(null)
                .build();
    }
}
