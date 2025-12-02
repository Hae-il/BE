package com.haeil.be.chatbot.dto.request;

import com.haeil.be.cases.domain.type.CaseType;
import java.time.LocalDateTime;

public record CreateChatReservationRequest(
        String name,
        String phone,
        CaseType caseType,
        String description,
        LocalDateTime requestDate) {}
