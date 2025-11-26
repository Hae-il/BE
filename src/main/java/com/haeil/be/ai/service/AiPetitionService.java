package com.haeil.be.ai.service;

import com.haeil.be.ai.dto.response.AiPetitionResponse;
import com.haeil.be.cases.domain.Cases;
import com.haeil.be.cases.exception.CasesException;
import com.haeil.be.cases.exception.errorcode.CasesErrorCode;
import com.haeil.be.cases.repository.CasesRepository;
import com.haeil.be.consultation.domain.Consultation;
import com.haeil.be.consultation.domain.ConsultationNote;
import com.haeil.be.consultation.repository.ConsultationNoteRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiPetitionService {

    private final PetitionAdvisor petitionAgent;
    private final CasesRepository casesRepository;
    private final ConsultationNoteRepository consultationNoteRepository;

    /**
     * 소장 초안을 생성합니다.
     *
     * @param caseId 사건 ID
     * @return 생성된 소장 초안 데이터
     */
    @Transactional(readOnly = true)
    public AiPetitionResponse generatePetitionDraft(Long caseId) {
        // 1. 데이터 조회
        Cases cases =
                casesRepository
                        .findById(caseId)
                        .orElseThrow(() -> new CasesException(CasesErrorCode.CASE_NOT_FOUND));

        Consultation consultation = cases.getConsultation();

        List<ConsultationNote> consultationNotes =
                consultation != null
                        ? consultationNoteRepository.findByConsultationIdOrderByCreatedDateDesc(
                                consultation.getId())
                        : List.of();

        // 2. 프롬프트 생성
        String promptMessage = buildPrompt(cases, consultation, consultationNotes);

        // 3. AI 호출 및 매핑 (LangChain4j)
        return petitionAgent.generateDraft(promptMessage);
    }

    private String buildPrompt(
            Cases cases, Consultation consultation, List<ConsultationNote> consultationNotes) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("다음 사건 정보를 바탕으로 소장 작성에 필요한 항목들을 추출하고 작성해주세요.\n\n");

        // 사건 정보
        prompt.append("## 사건 정보\n");
        if (cases.getTitle() != null) {
            prompt.append("- 사건명: ").append(cases.getTitle()).append("\n");
        }
        if (cases.getContent() != null) {
            prompt.append("- 사건 내용: ").append(cases.getContent()).append("\n");
        }
        if (cases.getCaseType() != null) {
            prompt.append("- 사건 유형: ").append(cases.getCaseType()).append("\n");
        }
        if (cases.getOpponentName() != null) {
            prompt.append("- 상대방 이름: ").append(cases.getOpponentName()).append("\n");
        }
        if (cases.getOccurredDate() != null) {
            prompt.append("- 사고 발생일: ").append(cases.getOccurredDate()).append("\n");
        }
        prompt.append("\n");

        // 상담 정보
        if (consultation != null && consultation.getConsultationDate() != null) {
            prompt.append("## 상담 정보\n");
            prompt.append("- 상담일: ").append(consultation.getConsultationDate()).append("\n\n");
        }

        // 상담 노트 정보
        if (consultationNotes != null && !consultationNotes.isEmpty()) {
            prompt.append("## 상담 노트\n");
            for (ConsultationNote note : consultationNotes) {
                if (note.getFactSummary() != null) {
                    prompt.append("- 사실 요약: ").append(note.getFactSummary()).append("\n");
                }
                if (note.getEvidenceSummary() != null) {
                    prompt.append("- 증거 요약: ").append(note.getEvidenceSummary()).append("\n");
                }
                if (note.getLegalIssue() != null) {
                    prompt.append("- 법적 쟁점: ").append(note.getLegalIssue()).append("\n");
                }
                if (note.getRelatedLaws() != null) {
                    prompt.append("- 관련 법령: ").append(note.getRelatedLaws()).append("\n");
                }
                if (note.getClientGoal() != null) {
                    prompt.append("- 의뢰인 목표: ").append(note.getClientGoal()).append("\n");
                }
                if (note.getLawyerOpinion() != null) {
                    prompt.append("- 변호사 의견: ").append(note.getLawyerOpinion()).append("\n");
                }
                if (note.getRiskAssessment() != null) {
                    prompt.append("- 위험 평가: ").append(note.getRiskAssessment()).append("\n");
                }
                prompt.append("\n");
            }
        }

        prompt.append(
                """
                위 정보를 바탕으로 소장의 각 항목(청구취지, 청구원인 등)을 법률 문서 스타일로 전문적으로 작성해주세요.
                특히 '청구원인'은 사고 경위, 책임의 근거, 손해 배상의 범위 등을 논리적으로 서술해야 합니다.
                """);

        return prompt.toString();
    }
}
