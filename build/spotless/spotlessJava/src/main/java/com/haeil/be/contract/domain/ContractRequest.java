package com.haeil.be.contract.domain;

import com.haeil.be.cases.domain.type.CaseType;
import com.haeil.be.consultation.domain.Consultation;
import com.haeil.be.contract.domain.type.ContractRequestStatus;
import com.haeil.be.global.entity.BaseEntity;
import jakarta.persistence.*;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "contract_request")
@Entity
@Getter
@NoArgsConstructor
public class ContractRequest extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "consultation_id", nullable = false)
    private Consultation consultation;

    @Enumerated(EnumType.STRING)
    @Column(name = "case_type", nullable = false)
    private CaseType caseType;

    @Column(name = "estimated_fee", precision = 15, scale = 2)
    private BigDecimal estimatedFee;

    @Column(name = "consultation_summary", columnDefinition = "TEXT")
    private String consultationSummary;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ContractRequestStatus status;
}
