package com.haeil.full.cases.dto.request;

import com.haeil.full.cases.domain.type.EventType;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

public record CaseEventRequest(
        EventType eventType,
        String location,
        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime date) {}
