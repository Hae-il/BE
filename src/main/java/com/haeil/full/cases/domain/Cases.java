package com.haeil.full.cases.domain;

import com.haeil.full.cases.domain.type.CaseStatus;
import com.haeil.full.cases.domain.type.CaseType;
import com.haeil.full.client.domain.Client;
import com.haeil.full.consultation.domain.Consultation;
import com.haeil.full.global.entity.BaseEntity;
import com.haeil.full.user.domain.User;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "cases")
@Entity
@Getter
@NoArgsConstructor
public class Cases extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "case_number", unique = true)
    private String caseNumber;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "case_status")
    private CaseStatus caseStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "case_type")
    private CaseType caseType;

    @Column(name = "opponent_name")
    private String opponentName;

    @Column(name = "opponent_phone")
    private String opponentPhone;

    @Column(name = "opponent_insurance")
    private String opponentInsurance;

    @Column(name = "occurred_date")
    private LocalDateTime occurredDate;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "consultation_id")
    private Consultation consultation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attorney_id")
    private User attorney;

    @OneToMany(
            mappedBy = "cases",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<CaseDocument> caseDocumentList = new ArrayList<>();

    @OneToMany(
            mappedBy = "cases",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<CaseEvent> caseEventList = new ArrayList<>();

    @OneToOne(
            mappedBy = "cases",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private Petition petition;

    @Builder
    public Cases(
            String title,
            String content,
            CaseStatus caseStatus,
            CaseType caseType,
            String opponentName,
            String opponentPhone,
            String opponentInsurance,
            LocalDateTime occurredDate,
            User attorney,
            Consultation consultation,
            Client client) {
        this.title = title;
        this.content = content;
        this.caseStatus = caseStatus;
        this.caseType = caseType;
        this.opponentName = opponentName;
        this.opponentPhone = opponentPhone;
        this.opponentInsurance = opponentInsurance;
        this.occurredDate = occurredDate;
        this.attorney = attorney;
        this.consultation = consultation;
        this.client = client;
    }

    public void assignAttorney(User attorney) {
        this.attorney = attorney;
    }

    public void removeAttorney() {
        this.attorney = null;
    }

    public void updateStatus(CaseStatus caseStatus) {
        this.caseStatus = caseStatus;
    }

    public void updateCaseNumber(String caseNumber) {
        this.caseNumber = caseNumber;
    }
}
