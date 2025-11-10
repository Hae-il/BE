package com.haeil.be.consultation.domain;

import com.haeil.be.client.domain.Client;
import com.haeil.be.consultation.domain.type.ConsultationStatus;
import com.haeil.be.global.entity.BaseEntity;
import com.haeil.be.user.domain.User;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "consultation")
@Entity
@Getter
@NoArgsConstructor
public class Consultation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "consult_req_id", nullable = false)
    private ConsultationRequest consultationRequest;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "consult_lawyer_id", nullable = false)
    private User consultLawyer;

    @Column(name = "consultation_date")
    private LocalDateTime consultationDate;

    @Column(name = "location")
    private String location;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ConsultationStatus status;

    @OneToMany(mappedBy = "consultation", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ConsultationNote> consultationNotes = new ArrayList<>();

    @Builder
    public Consultation(
            ConsultationRequest consultationRequest,
            Client client,
            User consultLawyer,
            LocalDateTime consultationDate,
            String location) {
        this.consultationRequest = consultationRequest;
        this.client = client;
        this.consultLawyer = consultLawyer;
        this.consultationDate = consultationDate;
        this.location = location;
        this.status = ConsultationStatus.CONSULTATION_WAITING;
    }

    public void startConsultation() {
        validateStatusTransition(ConsultationStatus.CONSULTATION_IN_PROGRESS);
        this.status = ConsultationStatus.CONSULTATION_IN_PROGRESS;
    }

    public void completeConsultation() {
        validateStatusTransition(ConsultationStatus.CONTRACT_PENDING);
        this.status = ConsultationStatus.CONTRACT_PENDING;
    }

    public void startContract() {
        validateStatusTransition(ConsultationStatus.CONTRACT_IN_PROGRESS);
        this.status = ConsultationStatus.CONTRACT_IN_PROGRESS;
    }

    public void completeContract() {
        validateStatusTransition(ConsultationStatus.COMPLETED);
        this.status = ConsultationStatus.COMPLETED;
    }

    private void validateStatusTransition(ConsultationStatus newStatus) {
        if (!isValidTransition(newStatus)) {
            throw new IllegalStateException(
                    String.format("상담 상태를 %s에서 %s로 변경할 수 없습니다.", this.status, newStatus));
        }
    }

    private boolean isValidTransition(ConsultationStatus newStatus) {
        return switch (this.status) {
            case CONSULTATION_WAITING -> newStatus == ConsultationStatus.CONSULTATION_IN_PROGRESS;
            case CONSULTATION_IN_PROGRESS -> newStatus == ConsultationStatus.CONTRACT_PENDING;
            case CONTRACT_PENDING -> newStatus == ConsultationStatus.CONTRACT_IN_PROGRESS;
            case CONTRACT_IN_PROGRESS -> newStatus == ConsultationStatus.COMPLETED;
            case COMPLETED -> false;
        };
    }

    public boolean isInProgress() {
        return this.status == ConsultationStatus.CONSULTATION_IN_PROGRESS;
    }

    public boolean isCompleted() {
        return this.status == ConsultationStatus.COMPLETED;
    }

    public boolean canWriteNote() {
        return this.status == ConsultationStatus.CONSULTATION_IN_PROGRESS;
    }
}
