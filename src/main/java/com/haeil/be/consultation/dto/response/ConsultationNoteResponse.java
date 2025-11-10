package com.haeil.be.consultation.dto.response;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ConsultationNoteResponse {

    private Long id;
    private Long consultationId;
    private Long authorId;
    private String authorName;
    private String factSummary;
    private String evidenceSummary;
    private String legalIssue;
    private String relatedLaws;
    private String clientGoal;
    private String lawyerOpinion;
    private String riskAssessment;
    private String nextAction;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
