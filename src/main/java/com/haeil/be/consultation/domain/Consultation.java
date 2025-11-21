package com.haeil.be.consultation.domain;

import com.haeil.be.client.domain.Client;
import com.haeil.be.consultation.domain.type.ConsultationStatus;
import com.haeil.be.consultation.exception.ConsultationException;
import com.haeil.be.consultation.exception.errorcode.ConsultationErrorCode;
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
    @JoinColumn(name = "reservation_id", nullable = false)
    private ConsultationReservation consultationReservation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "counselor_id", nullable = false)
    private User counselor;

    @Column(name = "consultation_date")
    private LocalDateTime consultationDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ConsultationStatus status;

    @OneToMany(mappedBy = "consultation", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ConsultationNote> consultationNotes = new ArrayList<>();

    @Builder
    public Consultation(
            ConsultationReservation consultationReservation,
            Client client,
            User counselor,
            LocalDateTime consultationDate) {
        this.consultationReservation = consultationReservation;
        this.client = client;
        this.counselor = counselor;
        this.consultationDate = consultationDate;
        this.status = ConsultationStatus.IN_PROGRESS;
    }

    public void startConsultation() {
        this.status = ConsultationStatus.IN_PROGRESS;
    }

    public void completeConsultation() {
        if (this.status != ConsultationStatus.IN_PROGRESS) {
            throw new IllegalStateException("진행 중인 상담만 완료할 수 있습니다.");
        }
        this.status = ConsultationStatus.COMPLETED;
    }

    public boolean isInProgress() {
        return this.status == ConsultationStatus.IN_PROGRESS;
    }

    public boolean isCompleted() {
        return this.status == ConsultationStatus.COMPLETED;
    }

    public User getCounselor() {
        return this.counselor;
    }

    public void updateCounselor(User newCounselor) {
        if (!this.isInProgress()) {
            throw new ConsultationException(ConsultationErrorCode.CONSULTATION_NOT_IN_PROGRESS);
        }
        this.counselor = newCounselor;
    }
}
