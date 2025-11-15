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
                                consultation.getConsultationReservation().getName()
                                        + " - "
                                        + consultation
                                                .getConsultationReservation()
                                                .getCaseType()
                                                .getLabel()
                                        + " 사건")
                        .content(consultation.getConsultationReservation().getDescription())
                        .caseStatus(CaseStatus.UNSIGNED)
                        .caseType(consultation.getConsultationReservation().getCaseType())
                        .attorney(consultation.getCounselor())
                        .consultation(consultation)
                        .build();

        return casesRepository.save(newCase);
    }
}
