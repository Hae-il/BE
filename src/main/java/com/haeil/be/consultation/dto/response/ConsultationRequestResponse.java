package com.haeil.be.consultation.dto.response;

import com.haeil.be.cases.domain.type.CaseType;
import com.haeil.be.consultation.domain.type.ConsultationRequestStatus;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ConsultationRequestResponse {

    private Long id;
    private String name;
    private String phone;
    private LocalDateTime preferredDatetime;
    private CaseType caseType;
    private String briefDescription;
    private ConsultationRequestStatus status;
    private Long assignedLawyerId;
    private String assignedLawyerName;
    private String rejectReason;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
