package com.haeil.be.consultation.domain;

import com.haeil.be.global.entity.BaseEntity;
import com.haeil.be.user.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "consultation_note")
@Entity
@Getter
@NoArgsConstructor
public class ConsultationNote extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "consultation_id", nullable = false)
    private Consultation consultation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @Column(name = "fact_summary", columnDefinition = "TEXT")
    private String factSummary;

    @Column(name = "evidence_summary", columnDefinition = "TEXT")
    private String evidenceSummary;

    @Column(name = "legal_issue", columnDefinition = "TEXT")
    private String legalIssue;

    @Column(name = "related_laws", columnDefinition = "TEXT")
    private String relatedLaws;

    @Column(name = "client_goal", columnDefinition = "TEXT")
    private String clientGoal;

    @Column(name = "lawyer_opinion", columnDefinition = "TEXT")
    private String lawyerOpinion;

    @Column(name = "risk_assessment", columnDefinition = "TEXT")
    private String riskAssessment;

    @Column(name = "next_action", columnDefinition = "TEXT")
    private String nextAction;
}
