package com.haeil.full.consultation.dto.response;

import com.haeil.full.consultation.domain.ConsultationNote;
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

    public static ConsultationNoteResponse from(ConsultationNote consultationNote) {
        return ConsultationNoteResponse.builder()
                .id(consultationNote.getId())
                .consultationId(consultationNote.getConsultation().getId())
                .authorId(consultationNote.getAuthor().getId())
                .authorName(consultationNote.getAuthor().getName())
                .factSummary(consultationNote.getFactSummary())
                .evidenceSummary(consultationNote.getEvidenceSummary())
                .legalIssue(consultationNote.getLegalIssue())
                .relatedLaws(consultationNote.getRelatedLaws())
                .clientGoal(consultationNote.getClientGoal())
                .lawyerOpinion(consultationNote.getLawyerOpinion())
                .riskAssessment(consultationNote.getRiskAssessment())
                .nextAction(consultationNote.getNextAction())
                .createdAt(consultationNote.getCreatedDate())
                .updatedAt(consultationNote.getModifiedDate())
                .build();
    }
}
