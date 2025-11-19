package com.haeil.be.cases.service;

import static com.haeil.be.cases.exception.errorcode.CasesErrorCode.CASE_NOT_FOUND;

import com.haeil.be.cases.domain.CaseDocument;
import com.haeil.be.cases.domain.Cases;
import com.haeil.be.cases.domain.type.CaseStatus;
import com.haeil.be.cases.domain.Petition;
import com.haeil.be.cases.dto.request.AssignAttorneyRequest;
import com.haeil.be.cases.dto.request.CaseDocumentRequest;
import com.haeil.be.cases.dto.request.DecisionRequest;
import com.haeil.be.cases.dto.request.PetitionRequest;
import com.haeil.be.cases.dto.response.CaseDocumentResponse;
import com.haeil.be.cases.dto.response.CaseInfoResponse;
import com.haeil.be.cases.dto.response.PetitionResponse;
import com.haeil.be.cases.dto.response.OngoingCaseDetailResponse;
import com.haeil.be.cases.dto.response.OngoingCaseResponse;
import com.haeil.be.cases.dto.response.RequestedCaseDetailResponse;
import com.haeil.be.cases.dto.response.RequestedCaseResponse;
import com.haeil.be.cases.dto.response.UnassignedCaseDetailResponse;
import com.haeil.be.cases.dto.response.UnassignedCaseResponse;
import com.haeil.be.cases.exception.CasesException;
import com.haeil.be.cases.exception.errorcode.CasesErrorCode;
import com.haeil.be.cases.repository.CaseDocumentRepository;
import com.haeil.be.cases.repository.CasesRepository;
import com.haeil.be.cases.repository.PetitionRepository;
import com.haeil.be.consultation.domain.Consultation;
import com.haeil.be.file.domain.FileEntity;
import com.haeil.be.file.repository.FileRepository;
import com.haeil.be.file.service.FileService;
import com.haeil.be.user.domain.User;
import com.haeil.be.user.repository.UserRepository;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class CasesService {

    private final CasesRepository casesRepository;
    private final PetitionRepository petitionRepository;
    private final CaseDocumentRepository caseDocumentRepository;
    private final FileService fileService;
    private final FileRepository fileRepository;

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

    // 소장 작성
    @Transactional
    public void createPetition(Long caseId, PetitionRequest request, Long userId) {
        Cases foundCase = getCasesOrThrow(caseId);

        if (foundCase.getCaseStatus() != CaseStatus.IN_PROGRESS) {
            throw new CasesException(CasesErrorCode.INVALID_ATTORNEY_ASSIGN);
        }

        // 변호사는 본인 담당 사건만 소장 작성 가능
        if (foundCase.getAttorney() == null || !foundCase.getAttorney().getId().equals(userId)) {
            throw new CasesException(CasesErrorCode.INVALID_ATTORNEY_ASSIGN);
        }

        // 이미 소장이 작성되었는지 확인
        if (foundCase.getPetition() != null) {
            throw new CasesException(CasesErrorCode.PETITION_ALREADY_EXISTS);
        }

        Petition petition = Petition.builder()
                .cases(foundCase)
                .claimAmount(request.claimAmount())
                .claimContent(request.claimContent())
                .accidentCircumstances(request.accidentCircumstances())
                .damageItems(request.damageItems())
                .damageCalculation(request.damageCalculation())
                .liabilityBasis(request.liabilityBasis())
                .proofMethod(request.proofMethod())
                .attachedDocuments(request.attachedDocuments())
                .build();

        petitionRepository.save(petition);
    }

    // 소장 조회
    @Transactional(readOnly = true)
    public PetitionResponse getPetition(Long caseId, Long userId) {
        Cases foundCase = getCasesOrThrow(caseId);

        if (foundCase.getCaseStatus() != CaseStatus.IN_PROGRESS) {
            throw new CasesException(CasesErrorCode.INVALID_ATTORNEY_ASSIGN);
        }

        // 변호사는 본인 담당 사건만 소장 조회 가능
        if (foundCase.getAttorney() == null || !foundCase.getAttorney().getId().equals(userId)) {
            throw new CasesException(CasesErrorCode.INVALID_ATTORNEY_ASSIGN);
        }

        if (foundCase.getPetition() == null) {
            throw new CasesException(CasesErrorCode.PETITION_NOT_FOUND);
        }

        return PetitionResponse.from(foundCase.getPetition());
    }

    // 소장 수정
    @Transactional
    public void updatePetition(Long caseId, PetitionRequest request, Long userId) {
        Cases foundCase = getCasesOrThrow(caseId);

        if (foundCase.getCaseStatus() != CaseStatus.IN_PROGRESS) {
            throw new CasesException(CasesErrorCode.INVALID_ATTORNEY_ASSIGN);
        }

        // 변호사는 본인 담당 사건만 소장 수정 가능
        if (foundCase.getAttorney() == null || !foundCase.getAttorney().getId().equals(userId)) {
            throw new CasesException(CasesErrorCode.INVALID_ATTORNEY_ASSIGN);
        }

        Petition petition = foundCase.getPetition();
        if (petition == null) {
            throw new CasesException(CasesErrorCode.PETITION_NOT_FOUND);
        }

        // 소장 정보 업데이트
        updatePetitionFields(petition, request);
    }

    private void updatePetitionFields(Petition petition, PetitionRequest request) {
        if (request.claimAmount() != null) {
            petition.updateClaimAmount(request.claimAmount());
        }
        if (request.claimContent() != null) {
            petition.updateClaimContent(request.claimContent());
        }
        if (request.accidentCircumstances() != null) {
            petition.updateAccidentCircumstances(request.accidentCircumstances());
        }
        if (request.damageItems() != null) {
            petition.updateDamageItems(request.damageItems());
        }
        if (request.damageCalculation() != null) {
            petition.updateDamageCalculation(request.damageCalculation());
        }
        if (request.liabilityBasis() != null) {
            petition.updateLiabilityBasis(request.liabilityBasis());
        }
        if (request.proofMethod() != null) {
            petition.updateProofMethod(request.proofMethod());
        }
        if (request.attachedDocuments() != null) {
            petition.updateAttachedDocuments(request.attachedDocuments());
        }
    }

    //법원에서 할당된 사건번호 추가
    @Transactional
    public void updateCaseNumber(Long caseId, String caseNumber, Long userId) {
        Cases foundCase = getCasesOrThrow(caseId);

        // 담당 변호사만 가능
        if (foundCase.getAttorney() == null || !foundCase.getAttorney().getId().equals(userId)) {
            throw new CasesException(CasesErrorCode.INVALID_ATTORNEY_ASSIGN);
        }

        // 사건번호 업데이트
        foundCase.updateCaseNumber(caseNumber);
    }

    // 소송문서 업로드
    @Transactional
    public CaseDocumentResponse uploadCaseDocument(
            Long caseId, MultipartFile file, CaseDocumentRequest request, Long userId) throws IOException {
        Cases foundCase = getCasesOrThrow(caseId);

        if (foundCase.getCaseStatus() != CaseStatus.IN_PROGRESS) {
            throw new CasesException(CasesErrorCode.INVALID_ATTORNEY_ASSIGN);
        }

        // 변호사는 본인 담당 사건만 소송문서 업로드 가능
        if (foundCase.getAttorney() == null || !foundCase.getAttorney().getId().equals(userId)) {
            throw new CasesException(CasesErrorCode.INVALID_ATTORNEY_ASSIGN);
        }

        // 파일 검증
        if (file == null || file.isEmpty()) {
            throw new CasesException(CasesErrorCode.FILE_REQUIRED);
        }

        // 파일 업로드
        FileEntity uploadedFile;
        try {
            uploadedFile = fileService.uploadFile(file);
        } catch (IOException e) {
            throw new CasesException(CasesErrorCode.FILE_UPLOAD_FAILED);
        }

        // 소송문서 생성
        CaseDocument caseDocument = CaseDocument.builder()
                .cases(foundCase)
                .documentType(request.documentType())
                .file(uploadedFile)
                .description(request.description())
                .build();

        caseDocumentRepository.save(caseDocument);

        return CaseDocumentResponse.from(caseDocument);
    }

    // 소송문서 목록 조회
    @Transactional(readOnly = true)
    public List<CaseDocumentResponse> getCaseDocuments(Long caseId, Long userId) {
        Cases foundCase = getCasesOrThrow(caseId);

        if (foundCase.getCaseStatus() != CaseStatus.IN_PROGRESS) {
            throw new CasesException(CasesErrorCode.INVALID_ATTORNEY_ASSIGN);
        }

        // 변호사는 본인 담당 사건만 소송문서 조회 가능
        if (foundCase.getAttorney() == null || !foundCase.getAttorney().getId().equals(userId)) {
            throw new CasesException(CasesErrorCode.INVALID_ATTORNEY_ASSIGN);
        }

        List<CaseDocument> documents = caseDocumentRepository.findByCasesId(caseId);
        return documents.stream()
                .map(CaseDocumentResponse::from)
                .collect(Collectors.toList());
    }

    // 소송문서 삭제
    @Transactional
    public void deleteCaseDocument(Long caseId, Long documentId, Long userId) throws IOException {
        Cases foundCase = getCasesOrThrow(caseId);

        if (foundCase.getCaseStatus() != CaseStatus.IN_PROGRESS) {
            throw new CasesException(CasesErrorCode.INVALID_ATTORNEY_ASSIGN);
        }

        // 변호사는 본인 담당 사건만 소송문서 삭제 가능
        if (foundCase.getAttorney() == null || !foundCase.getAttorney().getId().equals(userId)) {
            throw new CasesException(CasesErrorCode.INVALID_ATTORNEY_ASSIGN);
        }

        CaseDocument caseDocument = caseDocumentRepository.findById(documentId)
                .orElseThrow(() -> new CasesException(CasesErrorCode.CASE_DOCUMENT_NOT_FOUND));

        // 해당 사건의 문서인지 확인
        if (!caseDocument.getCases().getId().equals(caseId)) {
            throw new CasesException(CasesErrorCode.CASE_DOCUMENT_NOT_FOUND);
        }

        // 파일 삭제
        FileEntity file = caseDocument.getFile();
        if (file != null) {
            try {
                Path filePath = Paths.get(file.getFileUrl());
                if (Files.exists(filePath)) {
                    Files.delete(filePath);
                }
                fileRepository.delete(file);
            } catch (IOException e) {
                throw new CasesException(CasesErrorCode.FILE_DELETE_FAILED);
            }
        }

        // 소송문서 삭제
        caseDocumentRepository.delete(caseDocument);
    }

}
