package com.haeil.be.cases.service;

import com.haeil.be.cases.domain.Cases;
import com.haeil.be.cases.domain.type.CaseStatus;
import com.haeil.be.cases.dto.request.AssignAttorneyRequest;
import com.haeil.be.cases.dto.response.UnassignedCaseDetailResponse;
import com.haeil.be.cases.dto.response.UnassignedCaseResponse;
import com.haeil.be.cases.exception.CasesException;
import com.haeil.be.cases.exception.errorcode.CasesErrorCode;
import com.haeil.be.cases.repository.CasesRepository;
import com.haeil.be.user.domain.User;
import com.haeil.be.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class CasesService {

    private final CasesRepository casesRepository;
    private final UserRepository userRepository;

    //미배정 사건 목록조회
    public List<UnassignedCaseResponse> getUnassignedCases() {
        return casesRepository.findByCaseStatus(CaseStatus.UNASSIGNED)
                .stream()
                .map(UnassignedCaseResponse::from)
                .toList();
    }

    //미배정사건 상세보기
    public UnassignedCaseDetailResponse getUnassignedCaseDetail(Long caseId) {
        Cases foundCase = casesRepository.findById(caseId)
                .orElseThrow(() -> new CasesException(CasesErrorCode.CASE_NOT_FOUND));

        if (foundCase.getCaseStatus() != CaseStatus.UNASSIGNED) {
            throw new CasesException(CasesErrorCode.NOT_UNASSIGNED_CASE);
        }

        return UnassignedCaseDetailResponse.from(foundCase);
    }

    //변호사 배정요청
    public void assignAttorney(Long caseId, AssignAttorneyRequest request) {
        Cases foundCase = casesRepository.findById(caseId)
                .orElseThrow(() -> new CasesException(CasesErrorCode.CASE_NOT_FOUND));

        if (foundCase.getCaseStatus() != CaseStatus.UNASSIGNED) {
            throw new CasesException(CasesErrorCode.INVALID_ATTORNEY_ASSIGN);
        }

        User attorney = userRepository.findById(request.attorneyId())
                .orElseThrow(() -> new CasesException(CasesErrorCode.ATTORNEY_NOT_FOUND));

        //변호사 배정요청
        foundCase.assignAttorney(attorney);

        //배정요청으로 상태변경
        foundCase.updateStatus(CaseStatus.PENDING);
    }
}
