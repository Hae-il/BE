package com.haeil.be.consultation.domain;

import com.haeil.be.cases.domain.type.CaseType;
import com.haeil.be.client.domain.Client;
import com.haeil.be.consultation.domain.type.ConsultationStatus;
import com.haeil.be.contract.domain.ContractRequest;
import com.haeil.be.global.entity.BaseEntity;
import com.haeil.be.user.domain.User;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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

    @ManyToOne(fetch = FetchType.LAZY)
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

    @Enumerated(EnumType.STRING)
    @Column(name = "case_type", nullable = false)
    private CaseType caseType;

    @Column(name = "location")
    private String location;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ConsultationStatus status;

    @Column(name = "estimated_fee", precision = 15, scale = 2)
    private BigDecimal estimatedFee;

    @Column(name = "reject_reason", columnDefinition = "TEXT")
    private String rejectReason;

    @OneToMany(mappedBy = "consultation", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ConsultationNote> consultationNotes = new ArrayList<>();

    @OneToMany(mappedBy = "consultation", fetch = FetchType.LAZY)
    private List<ContractRequest> contractRequests = new ArrayList<>();
}
