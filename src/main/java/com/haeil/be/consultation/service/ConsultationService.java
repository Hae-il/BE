package com.haeil.be.consultation.service;

import com.haeil.be.cases.service.CasesService;
import com.haeil.be.client.domain.Client;
import com.haeil.be.client.repository.ClientRepository;
import com.haeil.be.consultation.domain.Consultation;
import com.haeil.be.consultation.domain.ConsultationFile;
import com.haeil.be.consultation.domain.ConsultationNote;
import com.haeil.be.consultation.domain.ConsultationRequest;
import com.haeil.be.consultation.domain.type.ConsultationRequestStatus;
import com.haeil.be.consultation.dto.request.CreateConsultationDto;
import com.haeil.be.consultation.dto.request.CreateConsultationNoteDto;
import com.haeil.be.consultation.dto.request.CreateConsultationRequestDto;
import com.haeil.be.consultation.dto.request.UpdateConsultationNoteDto;
import com.haeil.be.consultation.dto.request.UpdateConsultationRequestStatusDto;
import com.haeil.be.consultation.dto.request.UpdateConsultationStatusDto;
import com.haeil.be.consultation.dto.response.ConsultationNoteResponse;
import com.haeil.be.consultation.dto.response.ConsultationRequestResponse;
import com.haeil.be.consultation.dto.response.ConsultationResponse;
import com.haeil.be.consultation.exception.ConsultationException;
import com.haeil.be.consultation.exception.errorcode.ConsultationErrorCode;
import com.haeil.be.consultation.repository.ConsultationFileRepository;
import com.haeil.be.consultation.repository.ConsultationNoteRepository;
import com.haeil.be.consultation.repository.ConsultationRepository;
import com.haeil.be.consultation.repository.ConsultationRequestRepository;
import com.haeil.be.file.domain.FileEntity;
import com.haeil.be.file.service.FileService;
import com.haeil.be.user.domain.User;
import com.haeil.be.user.repository.UserRepository;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ConsultationService {

    private final ConsultationRequestRepository consultationRequestRepository;
    private final ConsultationRepository consultationRepository;
    private final ConsultationNoteRepository consultationNoteRepository;
    private final ClientRepository clientRepository;
    private final UserRepository userRepository;
    private final FileService fileService;
    private final ConsultationFileRepository consultationFileRepository;
    private final CasesService casesService;

    @Transactional
    public ConsultationRequestResponse createConsultationRequest(
            CreateConsultationRequestDto request) {
        ConsultationRequest consultationRequest =
                ConsultationRequest.builder()
                        .name(request.getName())
                        .phone(request.getPhone())
                        .preferredDatetime(request.getPreferredDatetime())
                        .caseType(request.getCaseType())
                        .briefDescription(request.getBriefDescription())
                        .build();

        ConsultationRequest saved = consultationRequestRepository.save(consultationRequest);
        return mapToResponse(saved);
    }

    public List<ConsultationRequestResponse> getConsultationRequests() {
        return consultationRequestRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public ConsultationRequestResponse getConsultationRequest(Long id) {
        ConsultationRequest consultationRequest = findConsultationRequestById(id);
        return mapToResponse(consultationRequest);
    }

    @Transactional
    public ConsultationRequestResponse updateConsultationRequestStatus(
            Long id, UpdateConsultationRequestStatusDto request) {
        ConsultationRequest consultationRequest = findConsultationRequestById(id);

        if (request.getStatus() == ConsultationRequestStatus.APPROVED) {
            if (request.getAssignedLawyerId() == null) {
                throw new ConsultationException(ConsultationErrorCode.ASSIGNED_LAWYER_REQUIRED);
            }
            User assignedLawyer = findUserById(request.getAssignedLawyerId());
            consultationRequest.approve(assignedLawyer);
        } else if (request.getStatus() == ConsultationRequestStatus.REJECTED) {
            consultationRequest.reject(request.getRejectReason());
        }

        return mapToResponse(consultationRequest);
    }

    private ConsultationRequest findConsultationRequestById(Long id) {
        return consultationRequestRepository
                .findById(id)
                .orElseThrow(
                        () ->
                                new ConsultationException(
                                        ConsultationErrorCode.CONSULTATION_REQUEST_NOT_FOUND));
    }

    private User findUserById(Long id) {
        return userRepository
                .findById(id)
                .orElseThrow(() -> new ConsultationException(ConsultationErrorCode.USER_NOT_FOUND));
    }

    private ConsultationRequestResponse mapToResponse(ConsultationRequest consultationRequest) {
        return ConsultationRequestResponse.builder()
                .id(consultationRequest.getId())
                .name(consultationRequest.getName())
                .phone(consultationRequest.getPhone())
                .preferredDatetime(consultationRequest.getPreferredDatetime())
                .caseType(consultationRequest.getCaseType())
                .briefDescription(consultationRequest.getBriefDescription())
                .status(consultationRequest.getStatus())
                .assignedLawyerId(
                        consultationRequest.getConsultLawyer() != null
                                ? consultationRequest.getConsultLawyer().getId()
                                : null)
                .assignedLawyerName(
                        consultationRequest.getConsultLawyer() != null
                                ? consultationRequest.getConsultLawyer().getName()
                                : null)
                .rejectReason(consultationRequest.getRejectReason())
                .createdAt(consultationRequest.getCreatedDate())
                .updatedAt(consultationRequest.getModifiedDate())
                .build();
    }

    // Consultation Management Methods
    @Transactional
    public ConsultationResponse createConsultation(CreateConsultationDto request) {
        ConsultationRequest consultationRequest =
                findConsultationRequestById(request.getConsultationRequestId());

        if (!consultationRequest.isApproved()) {
            throw new ConsultationException(
                    ConsultationErrorCode.CONSULTATION_REQUEST_NOT_APPROVED);
        }

        Client client = findOrCreateClient(request.getClientInfo());
        User consultLawyer = consultationRequest.getConsultLawyer();

        Consultation consultation =
                Consultation.builder()
                        .consultationRequest(consultationRequest)
                        .client(client)
                        .consultLawyer(consultLawyer)
                        .consultationDate(request.getConsultationDate())
                        .location(request.getLocation())
                        .build();

        Consultation saved = consultationRepository.save(consultation);
        return mapToConsultationResponse(saved);
    }

    public List<ConsultationResponse> getConsultations() {
        return consultationRepository.findAll().stream()
                .map(this::mapToConsultationResponse)
                .collect(Collectors.toList());
    }

    public ConsultationResponse getConsultation(Long id) {
        Consultation consultation = findConsultationById(id);
        return mapToConsultationResponse(consultation);
    }

    @Transactional
    public ConsultationResponse updateConsultationStatus(
            Long id, UpdateConsultationStatusDto request) {
        Consultation consultation = findConsultationById(id);

        switch (request.getStatus()) {
            case CONSULTATION_IN_PROGRESS -> consultation.startConsultation();
            case COMPLETED -> {
                consultation.completeConsultation();
                casesService.createCaseFromConsultation(consultation);
            }
            default ->
                    throw new ConsultationException(
                            ConsultationErrorCode.INVALID_STATUS_TRANSITION);
        }

        return mapToConsultationResponse(consultation);
    }

    private Client findOrCreateClient(CreateConsultationDto.ClientInfoDto clientInfo) {
        if (clientInfo.getResidentNumber() != null) {
            return clientRepository
                    .findByResidentNumber(clientInfo.getResidentNumber())
                    .orElseGet(() -> createNewClient(clientInfo));
        }
        return createNewClient(clientInfo);
    }

    private Client createNewClient(CreateConsultationDto.ClientInfoDto clientInfo) {
        Client client =
                Client.fromRequest(
                        clientInfo.getName(),
                        clientInfo.getEmail(),
                        clientInfo.getPhone(),
                        clientInfo.getAddress(),
                        clientInfo.getResidentNumber(),
                        clientInfo.getBirthDate(),
                        clientInfo.getGender(),
                        clientInfo.getJobTitle());
        return clientRepository.save(client);
    }

    private Consultation findConsultationById(Long id) {
        return consultationRepository
                .findById(id)
                .orElseThrow(
                        () ->
                                new ConsultationException(
                                        ConsultationErrorCode.CONSULTATION_NOT_FOUND));
    }

    private ConsultationResponse mapToConsultationResponse(Consultation consultation) {
        return ConsultationResponse.builder()
                .id(consultation.getId())
                .consultationRequestId(consultation.getConsultationRequest().getId())
                .clientId(consultation.getClient().getId())
                .clientName(consultation.getClient().getName())
                .consultLawyerId(consultation.getConsultLawyer().getId())
                .consultLawyerName(consultation.getConsultLawyer().getName())
                .consultationDate(consultation.getConsultationDate())
                .location(consultation.getLocation())
                .status(consultation.getStatus())
                .createdAt(consultation.getCreatedDate())
                .updatedAt(consultation.getModifiedDate())
                .build();
    }

    // ConsultationNote Management Methods
    @Transactional
    public ConsultationNoteResponse createConsultationNote(
            Long consultationId, CreateConsultationNoteDto request) {
        Consultation consultation = findConsultationById(consultationId);

        if (!consultation.canWriteNote()) {
            throw new ConsultationException(ConsultationErrorCode.CONSULTATION_NOT_IN_PROGRESS);
        }

        ConsultationNote consultationNote =
                ConsultationNote.builder()
                        .consultation(consultation)
                        .author(consultation.getConsultLawyer())
                        .factSummary(request.getFactSummary())
                        .evidenceSummary(request.getEvidenceSummary())
                        .legalIssue(request.getLegalIssue())
                        .relatedLaws(request.getRelatedLaws())
                        .clientGoal(request.getClientGoal())
                        .lawyerOpinion(request.getLawyerOpinion())
                        .riskAssessment(request.getRiskAssessment())
                        .nextAction(request.getNextAction())
                        .build();

        ConsultationNote saved = consultationNoteRepository.save(consultationNote);
        return mapToConsultationNoteResponse(saved);
    }

    public List<ConsultationNoteResponse> getConsultationNotes(Long consultationId) {
        return consultationNoteRepository
                .findByConsultationIdOrderByCreatedDateDesc(consultationId)
                .stream()
                .map(this::mapToConsultationNoteResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public ConsultationNoteResponse updateConsultationNote(
            Long consultationId, Long noteId, UpdateConsultationNoteDto request) {
        ConsultationNote consultationNote = findConsultationNoteById(noteId);

        // 해당 상담의 노트인지 확인
        if (!consultationNote.getConsultation().getId().equals(consultationId)) {
            throw new ConsultationException(ConsultationErrorCode.CONSULTATION_NOTE_NOT_FOUND);
        }

        // 상담이 진행 중일 때만 수정 가능
        if (!consultationNote.getConsultation().canWriteNote()) {
            throw new ConsultationException(ConsultationErrorCode.CONSULTATION_NOT_IN_PROGRESS);
        }

        consultationNote.update(
                request.getFactSummary(),
                request.getEvidenceSummary(),
                request.getLegalIssue(),
                request.getRelatedLaws(),
                request.getClientGoal(),
                request.getLawyerOpinion(),
                request.getRiskAssessment(),
                request.getNextAction());

        return mapToConsultationNoteResponse(consultationNote);
    }

    private ConsultationNote findConsultationNoteById(Long id) {
        return consultationNoteRepository
                .findById(id)
                .orElseThrow(
                        () ->
                                new ConsultationException(
                                        ConsultationErrorCode.CONSULTATION_NOTE_NOT_FOUND));
    }

    private ConsultationNoteResponse mapToConsultationNoteResponse(
            ConsultationNote consultationNote) {
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

    // File Management Methods
    @Transactional
    public String uploadConsultationFile(
            Long consultationId, MultipartFile file, String description) throws IOException {
        Consultation consultation = findConsultationById(consultationId);

        FileEntity uploadedFile = fileService.uploadFile(file);

        ConsultationFile consultationFile =
                ConsultationFile.builder()
                        .consultation(consultation)
                        .file(uploadedFile)
                        .description(description)
                        .build();

        consultationFileRepository.save(consultationFile);

        return uploadedFile.getOriginalFilename() + " 업로드 완료";
    }

    public List<ConsultationFile> getConsultationFiles(Long consultationId) {
        return consultationFileRepository.findByConsultationId(consultationId);
    }
}
