package com.haeil.be.consultation.domain;

import com.haeil.be.cases.domain.type.CaseType;
import com.haeil.be.consultation.domain.type.ConsultationRequestStatus;
import com.haeil.be.global.entity.BaseEntity;
import com.haeil.be.user.domain.User;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "consultation_request")
@Entity
@Getter
@NoArgsConstructor
public class ConsultationRequest extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "preferred_datetime")
    private LocalDateTime preferredDatetime;

    @Enumerated(EnumType.STRING)
    @Column(name = "case_type", nullable = false)
    private CaseType caseType;

    @Column(name = "brief_description", columnDefinition = "TEXT")
    private String briefDescription;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ConsultationRequestStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "consult_lawyer_id")
    private User consultLawyer;

    @Column(name = "reject_reason", columnDefinition = "TEXT")
    private String rejectReason;

    @Builder
    public ConsultationRequest(
            String name,
            String phone,
            LocalDateTime preferredDatetime,
            CaseType caseType,
            String briefDescription) {
        this.name = name;
        this.phone = phone;
        this.preferredDatetime = preferredDatetime;
        this.caseType = caseType;
        this.briefDescription = briefDescription;
        this.status = ConsultationRequestStatus.PENDING;
    }

    public void approve(User assignedLawyer) {
        validateStatusTransition(ConsultationRequestStatus.APPROVED);
        this.status = ConsultationRequestStatus.APPROVED;
        this.consultLawyer = assignedLawyer;
        this.rejectReason = null;
    }

    public void reject(String rejectReason) {
        validateStatusTransition(ConsultationRequestStatus.REJECTED);
        this.status = ConsultationRequestStatus.REJECTED;
        this.rejectReason = rejectReason;
        this.consultLawyer = null;
    }

    private void validateStatusTransition(ConsultationRequestStatus newStatus) {
        if (this.status != ConsultationRequestStatus.PENDING) {
            throw new IllegalStateException("예약 상태는 PENDING 상태에서만 변경할 수 있습니다.");
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
}
