package com.haeil.be.consultation.dto.response;

import com.haeil.be.consultation.domain.Consultation;
import com.haeil.be.consultation.domain.type.ConsultationStatus;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ConsultationResponse {

    private Long id;
    private Long reservationId;
    private Long clientId;
    private String clientName;
    private Long counselorId;
    private String counselorName;
    private LocalDateTime consultationDate;
    private ConsultationStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ConsultationResponse from(Consultation consultation) {
        return ConsultationResponse.builder()
                .id(consultation.getId())
                .reservationId(consultation.getConsultationReservation().getId())
                .clientId(consultation.getClient().getId())
                .clientName(consultation.getClient().getName())
                .counselorId(consultation.getCounselor().getId())
                .counselorName(consultation.getCounselor().getName())
                .consultationDate(consultation.getConsultationDate())
                .status(consultation.getStatus())
                .createdAt(consultation.getCreatedDate())
                .updatedAt(consultation.getModifiedDate())
                .build();
    }
}
