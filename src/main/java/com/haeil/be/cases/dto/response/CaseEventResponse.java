package com.haeil.be.cases.dto.response;

import com.haeil.be.cases.domain.CaseEvent;
import com.haeil.be.cases.domain.type.EventType;
import java.time.LocalDateTime;

public record CaseEventResponse(
        Long eventId,
        EventType eventType,
        String location,
        LocalDateTime date
) {
    public static CaseEventResponse from(CaseEvent caseEvent) {
        return new CaseEventResponse(
                caseEvent.getId(),
                caseEvent.getType(),
                caseEvent.getLocation(),
                caseEvent.getDate()
        );
    }
}

