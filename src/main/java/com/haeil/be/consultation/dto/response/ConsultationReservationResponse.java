package com.haeil.be.consultation.dto.response;

import com.haeil.be.cases.domain.type.CaseType;
import com.haeil.be.consultation.domain.ConsultationReservation;
import com.haeil.be.consultation.domain.type.ConsultationRequestStatus;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ConsultationReservationResponse {

    private Long id;
    private String name;
    private String phone;
    private LocalDateTime requestedDate;
    private CaseType caseType;
    private String description;
    private ConsultationRequestStatus status;
    private Long assignedLawyerId;
    private String assignedLawyerName;
    private String rejectReason;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ConsultationReservationResponse from(ConsultationReservation consultationReservation) {
        return ConsultationReservationResponse.builder()
                .id(consultationReservation.getId())
                .name(consultationReservation.getName())
                .phone(consultationReservation.getPhone())
                .requestedDate(consultationReservation.getRequestedDate())
                .caseType(consultationReservation.getCaseType())
                .description(consultationReservation.getDescription())
                .status(consultationReservation.getStatus())
                .assignedLawyerId(
                        consultationReservation.getCounselor() != null
                                ? consultationReservation.getCounselor().getId()
                                : null)
                .assignedLawyerName(
                        consultationReservation.getCounselor() != null
                                ? consultationReservation.getCounselor().getName()
                                : null)
                .rejectReason(consultationReservation.getRejectReason())
                .createdAt(consultationReservation.getCreatedDate())
                .updatedAt(consultationReservation.getModifiedDate())
                .build();
    }
}
