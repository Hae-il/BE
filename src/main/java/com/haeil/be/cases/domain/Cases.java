package com.haeil.be.cases.domain;

import com.haeil.be.cases.domain.type.CaseStatus;
import com.haeil.be.cases.domain.type.CaseType;
import com.haeil.be.global.entity.BaseEntity;
import com.haeil.be.user.domain.User;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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

    @Column(name = "consultation_id")
    private Long consultationId;

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
}
