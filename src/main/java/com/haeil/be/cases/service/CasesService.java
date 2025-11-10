package com.haeil.be.cases.service;

import com.haeil.be.cases.domain.Cases;
import com.haeil.be.cases.domain.type.CaseStatus;
import com.haeil.be.cases.repository.CasesRepository;
import com.haeil.be.consultation.domain.Consultation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CasesService {

    private final CasesRepository casesRepository;

    @Transactional
    public Cases createCaseFromConsultation(Consultation consultation) {
        Cases newCase =
                Cases.builder()
                        .title(
                                consultation.getConsultationRequest().getName()
                                        + " - "
                                        + consultation
                                                .getConsultationRequest()
                                                .getCaseType()
                                                .getLabel()
                                        + " 사건")
                        .content(consultation.getConsultationRequest().getBriefDescription())
                        .caseStatus(CaseStatus.UNSIGNED)
                        .caseType(consultation.getConsultationRequest().getCaseType())
                        .attorney(consultation.getConsultLawyer())
                        .consultationId(consultation.getId())
                        .build();

        return casesRepository.save(newCase);
    }
}
