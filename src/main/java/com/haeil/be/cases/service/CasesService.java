package com.haeil.be.cases.service;

import static com.haeil.be.cases.exception.errorcode.CasesErrorCode.CASE_NOT_FOUND;

import com.haeil.be.cases.domain.Cases;
import com.haeil.be.cases.domain.type.CaseStatus;
import com.haeil.be.cases.dto.request.AssignAttorneyRequest;
import com.haeil.be.cases.dto.request.DecisionRequest;
import com.haeil.be.cases.dto.response.CaseInfoResponse;
import com.haeil.be.cases.dto.response.OngoingCaseDetailResponse;
import com.haeil.be.cases.dto.response.OngoingCaseResponse;
import com.haeil.be.cases.dto.response.RequestedCaseDetailResponse;
import com.haeil.be.cases.dto.response.RequestedCaseResponse;
import com.haeil.be.cases.dto.response.UnassignedCaseDetailResponse;
import com.haeil.be.cases.dto.response.UnassignedCaseResponse;
import com.haeil.be.cases.exception.CasesException;
import com.haeil.be.cases.exception.errorcode.CasesErrorCode;
import com.haeil.be.cases.repository.CasesRepository;
import com.haeil.be.consultation.domain.Consultation;
import com.haeil.be.user.domain.User;
import com.haeil.be.user.repository.UserRepository;
import java.util.List;
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
                        .caseStatus(CaseStatus.UNASSIGNED)
                        .caseType(consultation.getConsultationReservation().getCaseType())
                        .attorney(consultation.getCounselor())
                        .consultation(consultation)
                        .build();

        return casesRepository.save(newCase);
    }

    private final UserRepository userRepository;

    // 미배정 사건 목록조회
    public List<UnassignedCaseResponse> getUnassignedCases() {
        return casesRepository.findByCaseStatus(CaseStatus.UNASSIGNED).stream()
                .map(UnassignedCaseResponse::from)
                .toList();
    }

    // 미배정사건 상세보기
    public UnassignedCaseDetailResponse getUnassignedCaseDetail(Long caseId) {
        Cases foundCase =
                casesRepository
                        .findById(caseId)
                        .orElseThrow(() -> new CasesException(CASE_NOT_FOUND));

        if (foundCase.getCaseStatus() != CaseStatus.UNASSIGNED) {
            throw new CasesException(CasesErrorCode.ALREADY_ASSIGNED_CASE);
        }

        return UnassignedCaseDetailResponse.from(foundCase);
    }

    // 변호사 배정요청
    public void assignAttorney(Long caseId, AssignAttorneyRequest request) {
        Cases foundCase =
                casesRepository
                        .findById(caseId)
                        .orElseThrow(() -> new CasesException(CASE_NOT_FOUND));

        if (foundCase.getCaseStatus() != CaseStatus.UNASSIGNED) {
            throw new CasesException(CasesErrorCode.INVALID_ATTORNEY_ASSIGN);
        }

        User attorney =
                userRepository
                        .findById(request.attorneyId())
                        .orElseThrow(() -> new CasesException(CasesErrorCode.ATTORNEY_NOT_FOUND));

        // 변호사 배정요청
        foundCase.assignAttorney(attorney);

        // 배정요청으로 상태변경
        foundCase.updateStatus(CaseStatus.PENDING);
    }

    // 요청된 사건 목록조회
    public List<RequestedCaseResponse> getRequestedCases(Long userId) {
        User attorney =
                userRepository
                        .findById(userId)
                        .orElseThrow(() -> new CasesException(CasesErrorCode.ATTORNEY_NOT_FOUND));

        return casesRepository.findByCaseStatusAndAttorney(CaseStatus.PENDING, attorney).stream()
                .map(RequestedCaseResponse::from)
                .toList();
    }

    // 요청된 사건 상세보기
    public RequestedCaseDetailResponse getRequestedCaseDetail(Long caseId, Long userId) {
        Cases foundCase =
                casesRepository
                        .findById(caseId)
                        .orElseThrow(() -> new CasesException(CASE_NOT_FOUND));

        if (foundCase.getCaseStatus() != CaseStatus.PENDING) {
            throw new CasesException(CasesErrorCode.INVALID_ATTORNEY_ASSIGN);
        }

        // 요청받은 변호사만 조회 가능
        if (foundCase.getAttorney() == null || !foundCase.getAttorney().getId().equals(userId)) {
            throw new CasesException(CasesErrorCode.INVALID_ATTORNEY_ASSIGN);
        }

        return RequestedCaseDetailResponse.from(foundCase);
    }

    // 사건 배정 승인/거절
    public void decideCaseAssignment(Long caseId, DecisionRequest request, Long userId) {
        Cases foundCase =
                casesRepository
                        .findById(caseId)
                        .orElseThrow(() -> new CasesException(CASE_NOT_FOUND));

        if (foundCase.getCaseStatus() != CaseStatus.PENDING) {
            throw new CasesException(CasesErrorCode.INVALID_ATTORNEY_ASSIGN);
        }

        // 요청받은 변호사만 승인/거절 가능
        if (foundCase.getAttorney() == null || !foundCase.getAttorney().getId().equals(userId)) {
            throw new CasesException(CasesErrorCode.INVALID_ATTORNEY_ASSIGN);
        }

        if (request.isApproved()) {
            // 승인 시 진행중 사건목록으로 사건이동
            foundCase.updateStatus(CaseStatus.IN_PROGRESS);
        } else {
            // 거절 시 미배정 사건목록으로 반환
            foundCase.removeAttorney();
            foundCase.updateStatus(CaseStatus.UNASSIGNED);
        }
    }

    // 진행중인 사건 목록조회
    public List<OngoingCaseResponse> getOngoingCases(Long userId) {
        User attorney =
                userRepository
                        .findById(userId)
                        .orElseThrow(() -> new CasesException(CasesErrorCode.ATTORNEY_NOT_FOUND));

        return casesRepository
                .findByCaseStatusAndAttorney(CaseStatus.IN_PROGRESS, attorney)
                .stream()
                .map(OngoingCaseResponse::from)
                .toList();
    }

    // 진행중인 사건 상세보기
    public OngoingCaseDetailResponse getOngoingCaseDetail(Long caseId, Long userId) {
        Cases foundCase =
                casesRepository
                        .findById(caseId)
                        .orElseThrow(() -> new CasesException(CASE_NOT_FOUND));

        if (foundCase.getCaseStatus() != CaseStatus.IN_PROGRESS) {
            throw new CasesException(CasesErrorCode.INVALID_ATTORNEY_ASSIGN);
        }

        // 변호사는 본인 담당 사건만 확인 가능
        if (foundCase.getAttorney() == null || !foundCase.getAttorney().getId().equals(userId)) {
            throw new CasesException(CasesErrorCode.INVALID_ATTORNEY_ASSIGN);
        }

        return OngoingCaseDetailResponse.from(foundCase);
    }

    @Transactional(readOnly = true)
    public CaseInfoResponse getCaseInfo(Long caseId) {
        Cases cases = getCasesOrThrow(caseId);
        return CaseInfoResponse.from(cases);
    }

    private Cases getCasesOrThrow(Long caseId) {
        return casesRepository
                .findById(caseId)
                .orElseThrow(() -> new CasesException(CASE_NOT_FOUND));
    }
}
