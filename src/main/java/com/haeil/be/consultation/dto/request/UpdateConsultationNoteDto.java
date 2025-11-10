package com.haeil.be.consultation.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateConsultationNoteDto {

    private String factSummary;
    private String evidenceSummary;
    private String legalIssue;
    private String relatedLaws;
    private String clientGoal;
    private String lawyerOpinion;
    private String riskAssessment;
    private String nextAction;
}
