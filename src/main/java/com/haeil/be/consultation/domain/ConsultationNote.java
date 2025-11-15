package com.haeil.be.consultation.domain;

import com.haeil.be.global.entity.BaseEntity;
import com.haeil.be.user.domain.User;
import jakarta.persistence.*;
import lombok.Builder;
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

    @Builder
    public ConsultationNote(
            Consultation consultation,
            User author,
            String factSummary,
            String evidenceSummary,
            String legalIssue,
            String relatedLaws,
            String clientGoal,
            String lawyerOpinion,
            String riskAssessment,
            String nextAction) {
        this.consultation = consultation;
        this.author = author;
        this.factSummary = factSummary;
        this.evidenceSummary = evidenceSummary;
        this.legalIssue = legalIssue;
        this.relatedLaws = relatedLaws;
        this.clientGoal = clientGoal;
        this.lawyerOpinion = lawyerOpinion;
        this.riskAssessment = riskAssessment;
        this.nextAction = nextAction;
    }

    public void update(
            String factSummary,
            String evidenceSummary,
            String legalIssue,
            String relatedLaws,
            String clientGoal,
            String lawyerOpinion,
            String riskAssessment,
            String nextAction) {
        if (factSummary != null) this.factSummary = factSummary;
        if (evidenceSummary != null) this.evidenceSummary = evidenceSummary;
        if (legalIssue != null) this.legalIssue = legalIssue;
        if (relatedLaws != null) this.relatedLaws = relatedLaws;
        if (clientGoal != null) this.clientGoal = clientGoal;
        if (lawyerOpinion != null) this.lawyerOpinion = lawyerOpinion;
        if (riskAssessment != null) this.riskAssessment = riskAssessment;
        if (nextAction != null) this.nextAction = nextAction;
    }
}
