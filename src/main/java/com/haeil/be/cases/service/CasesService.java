package com.haeil.be.cases.service;

import static com.haeil.be.cases.exception.errorcode.CasesErrorCode.CASES_NOT_FOUND;

import com.haeil.be.cases.domain.Cases;
import com.haeil.be.cases.dto.response.CaseInfoResponse;
import com.haeil.be.cases.exception.CasesException;
import com.haeil.be.cases.repository.CasesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CasesService {

    private final CasesRepository casesRepository;

    @Transactional(readOnly = true)
    public CaseInfoResponse getCaseInfo(Long caseId) {
        Cases cases = getCasesOrThrow(caseId);
        return CaseInfoResponse.from(cases);
    }

    private Cases getCasesOrThrow(Long caseId) {
        return casesRepository
                .findById(caseId)
                .orElseThrow(() -> new CasesException(CASES_NOT_FOUND));
    }
}
