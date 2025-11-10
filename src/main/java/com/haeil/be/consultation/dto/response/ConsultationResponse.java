package com.haeil.be.consultation.dto.response;

import com.haeil.be.consultation.domain.type.ConsultationStatus;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ConsultationResponse {

    private Long id;
    private Long consultationRequestId;
    private Long clientId;
    private String clientName;
    private Long consultLawyerId;
    private String consultLawyerName;
    private LocalDateTime consultationDate;
    private String location;
    private ConsultationStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
