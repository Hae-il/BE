package com.haeil.be.consultation.domain;

import com.haeil.be.cases.domain.type.CaseType;
import com.haeil.be.consultation.domain.type.ConsultationRequestStatus;
import com.haeil.be.global.entity.BaseEntity;
import com.haeil.be.user.domain.User;
import jakarta.persistence.*;
import java.time.LocalDateTime;
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
    @JoinColumn(name = "counselor_id")
    private User counselor;

    @Column(name = "reject_reason", columnDefinition = "TEXT")
    private String rejectReason;
}
