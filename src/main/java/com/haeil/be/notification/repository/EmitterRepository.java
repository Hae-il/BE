package com.haeil.be.notification.repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Repository
public class EmitterRepository {
    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    public SseEmitter save(String id, SseEmitter sseEmitter) {
        emitters.put(id, sseEmitter);
        return sseEmitter;
    }

    public void deleteById(String id) {
        emitters.remove(id);
    }

    public Map<String, SseEmitter> findAllStartWithById(String id) {
        Map<String, SseEmitter> result = new ConcurrentHashMap<>();
        emitters.forEach(
                (key, emitter) -> {
                    if (key.startsWith(id)) {
                        result.put(key, emitter);
                    }
                });
        return result;
    }
}
