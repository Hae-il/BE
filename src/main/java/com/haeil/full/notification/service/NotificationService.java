package com.haeil.full.notification.service;

import com.haeil.full.notification.domain.Notification;
import com.haeil.full.notification.domain.type.NotificationType;
import com.haeil.full.notification.dto.response.NotificationResponse;
import com.haeil.full.notification.exception.NotificationException;
import com.haeil.full.notification.exception.errorcode.NotificationErrorCode;
import com.haeil.full.notification.repository.EmitterRepository;
import com.haeil.full.notification.repository.NotificationRepository;
import com.haeil.full.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60; // 1시간

    private final EmitterRepository emitterRepository;
    private final NotificationRepository notificationRepository;

    public SseEmitter subscribe(Long userId) {
        log.info("Subscribing user {}", userId);
        String id = userId + "_" + System.currentTimeMillis();
        SseEmitter emitter = emitterRepository.save(id, new SseEmitter(DEFAULT_TIMEOUT));

        emitter.onCompletion(() -> emitterRepository.deleteById(id));
        emitter.onTimeout(() -> emitterRepository.deleteById(id));

        // 503 에러 방지를 위한 더미 데이터 전송
        sendToClient(emitter, id, "EventStream Created. [userId=" + userId + "]");

        return emitter;
    }

    @Transactional
    public void send(User receiver, NotificationType notificationType, String content, String url) {
        log.info("Attempting to send notification. Receiver ID: {}", receiver.getId());
        Notification notification = notificationRepository.save(createNotification(receiver, notificationType, content, url));
        String receiverId = String.valueOf(receiver.getId());

        Map<String, SseEmitter> emitters = emitterRepository.findAllStartWithById(receiverId);
        log.info("Found {} emitters for receiver {}", emitters.size(), receiverId);

        if (emitters.isEmpty()) {
            log.info("No active emitters found for user {}", receiverId);
        }

        emitters.forEach(
                (key, emitter) -> {
                    try {
                        log.info("Sending to emitter: {}", key);
                        emitterRepository.save(key, emitter);
                        sendToClient(emitter, key, NotificationResponse.from(notification));
                    } catch (Exception e) {
                        emitterRepository.deleteById(key);
                        log.error("Failed to send notification to client.", e);
                    }
                }
        );
    }

    private Notification createNotification(User receiver, NotificationType notificationType, String content, String url) {
        return Notification.builder()
                .receiver(receiver)
                .notificationType(notificationType)
                .content(content)
                .relatedUrl(url)
                .build();
    }

    private void sendToClient(SseEmitter emitter, String id, Object data) {
        try {
            emitter.send(SseEmitter.event()
                    .id(id)
                    .name("sse")
                    .data(data));
        } catch (IOException e) {
            emitterRepository.deleteById(id);
            log.error("SSE Connection Failed", e);
            throw new NotificationException(NotificationErrorCode.SSE_CONNECTION_ERROR);
        }
    }
}

