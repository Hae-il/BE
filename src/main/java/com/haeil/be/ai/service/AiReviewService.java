package com.haeil.be.ai.service;

import com.haeil.be.cases.domain.Cases;
import com.haeil.be.cases.domain.Petition;
import com.haeil.be.cases.exception.CasesException;
import com.haeil.be.cases.exception.errorcode.CasesErrorCode;
import com.haeil.be.cases.repository.CasesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AiReviewService {

    private final CasesRepository casesRepository;
    private final LegalAdvisor legalAdvisor;

    @Transactional(readOnly = true)
    public String reviewPetition(Long caseId) {
        Cases cases =
                casesRepository
                        .findById(caseId)
                        .orElseThrow(() -> new CasesException(CasesErrorCode.CASE_NOT_FOUND));

        Petition petition = cases.getPetition();
        if (petition == null) {
            throw new CasesException(CasesErrorCode.PETITION_NOT_FOUND);
        }

        // AI에게 보낼 프롬프트 구성
        String prompt = createReviewPrompt(cases, petition);

        return legalAdvisor.reviewPetition(prompt);
    }

    private String createReviewPrompt(Cases cases, Petition petition) {
        return String.format(
                """
                [사건 개요]
                사건명: %s
                사건 유형: %s

                [소장 내용]
                1. 청구 취지
                청구 금액: %d원
                내용: %s

                2. 청구 원인 (사고 경위)
                %s

                3. 손해배상 산출 근거
                - 손해 항목: %s
                - 산출 내역: %s
                - 책임 근거: %s

                4. 입증 방법
                %s
                """,
                cases.getTitle(),
                cases.getCaseType().getLabel(),
                petition.getClaimAmount(),
                petition.getClaimContent(),
                petition.getAccidentCircumstances(),
                petition.getDamageItems(),
                petition.getDamageCalculation(),
                petition.getLiabilityBasis(),
                petition.getProofMethod());
    }
}
