package com.haeil.full.cases.dto.request;

import com.haeil.full.cases.domain.type.CaseType;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

public record UpdateCaseRequest(
        String title,
        String content,
        CaseType caseType,
        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime occurredDate,
        String opponentName,
        String opponentPhone,
        String opponentInsurance) {}
