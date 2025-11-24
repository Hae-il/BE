package com.haeil.full.consultation.domain;

import com.haeil.full.cases.domain.type.CaseType;
import com.haeil.full.consultation.domain.type.ConsultationRequestStatus;
import com.haeil.full.consultation.exception.ConsultationException;
import com.haeil.full.consultation.exception.errorcode.ConsultationErrorCode;
import com.haeil.full.global.entity.BaseEntity;
import com.haeil.full.user.domain.User;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "consultation_request")
@Entity
@Getter
@NoArgsConstructor
public class ConsultationReservation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "requested_date")
    private LocalDateTime requestedDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "case_type", nullable = false)
    private CaseType caseType;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ConsultationRequestStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "counselor_id")
    private User counselor;

    @Column(name = "reject_reason", columnDefinition = "TEXT")
    private String rejectReason;

    @Builder
    public ConsultationReservation(
            String name,
            String phone,
            LocalDateTime requestedDate,
            CaseType caseType,
            String description) {
        this.name = name;
        this.phone = phone;
        this.requestedDate = requestedDate;
        this.caseType = caseType;
        this.description = description;
        this.status = ConsultationRequestStatus.PENDING;
    }

    public void approve(User lawyer) {
        validateStatusTransition();
        this.status = ConsultationRequestStatus.APPROVED;
        this.counselor = lawyer;
        this.rejectReason = null;
    }

    public void reject(String rejectReason) {
        validateStatusTransition();
        this.status = ConsultationRequestStatus.REJECTED;
        this.rejectReason = rejectReason;
        this.counselor = null;
    }

    private void validateStatusTransition() {
        if (!this.isPending()) {
            throw new ConsultationException(
                    ConsultationErrorCode.INVALID_CONSULTATION_RESERVATION_STATUS_TRANSITION);
        }
    }

    public boolean isPending() {
        return this.status == ConsultationRequestStatus.PENDING;
    }

    public boolean isApproved() {
        return this.status == ConsultationRequestStatus.APPROVED;
    }

    public boolean isRejected() {
        return this.status == ConsultationRequestStatus.REJECTED;
    }

    public LocalDateTime getRequestedDate() {
        return this.requestedDate;
    }

    public String getDescription() {
        return this.description;
    }

    public User getCounselor() {
        return this.counselor;
    }
}
