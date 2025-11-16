package com.haeil.be.consultation.dto.request;

import com.haeil.be.consultation.domain.Consultation;
import com.haeil.be.consultation.domain.ConsultationNote;
import com.haeil.be.user.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ConsultationNoteRequest {

    private String factSummary;
    private String evidenceSummary;
    private String legalIssue;
    private String relatedLaws;
    private String clientGoal;
    private String lawyerOpinion;
    private String riskAssessment;
    private String nextAction;

    public ConsultationNote toEntity(Consultation consultation, User author) {
        return ConsultationNote.builder()
                .consultation(consultation)
                .author(author)
                .factSummary(this.factSummary)
                .evidenceSummary(this.evidenceSummary)
                .legalIssue(this.legalIssue)
                .relatedLaws(this.relatedLaws)
                .clientGoal(this.clientGoal)
                .lawyerOpinion(this.lawyerOpinion)
                .riskAssessment(this.riskAssessment)
                .nextAction(this.nextAction)
                .build();
    }
}
