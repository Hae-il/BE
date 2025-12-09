package com.haeil.full.consultation.service;

import com.haeil.full.cases.domain.Cases;
import com.haeil.full.cases.service.CasesService;
import com.haeil.full.client.domain.Client;
import com.haeil.full.client.service.ClientService;
import com.haeil.full.consultation.domain.Consultation;
import com.haeil.full.consultation.domain.ConsultationFile;
import com.haeil.full.consultation.domain.ConsultationNote;
import com.haeil.full.consultation.domain.ConsultationReservation;
import com.haeil.full.consultation.domain.type.ConsultationRequestStatus;
import com.haeil.full.consultation.domain.type.ConsultationStatus;
import com.haeil.full.consultation.dto.request.ApproveConsultationReservation;
import com.haeil.full.consultation.dto.request.ConsultationNoteRequest;
import com.haeil.full.consultation.dto.request.CreateConsultationRequest;
import com.haeil.full.consultation.dto.request.CreateConsultationReservationRequest;
import com.haeil.full.consultation.dto.request.RejectConsultationReservation;
import com.haeil.full.consultation.dto.response.ConsultationNoteResponse;
import com.haeil.full.consultation.dto.response.ConsultationReservationResponse;
import com.haeil.full.consultation.dto.response.ConsultationResponse;
import com.haeil.full.consultation.exception.ConsultationException;
import com.haeil.full.consultation.exception.errorcode.ConsultationErrorCode;
import com.haeil.full.consultation.repository.ConsultationFileRepository;
import com.haeil.full.consultation.repository.ConsultationNoteRepository;
import com.haeil.full.consultation.repository.ConsultationRepository;
import com.haeil.full.consultation.repository.ConsultationReservationRepository;
import com.haeil.full.file.domain.FileEntity;
import com.haeil.full.file.service.FileService;
import com.haeil.full.notification.domain.type.NotificationType;
import com.haeil.full.notification.service.NotificationService;
import com.haeil.full.user.domain.User;
import com.haeil.full.user.domain.type.Role;
import com.haeil.full.user.repository.UserRepository;
import com.haeil.full.user.service.UserService;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ConsultationService {

    private final ConsultationReservationRepository consultationReservationRepository;
    private final ConsultationRepository consultationRepository;
    private final ConsultationNoteRepository consultationNoteRepository;
    private final ClientService clientService;
    private final UserService userService;
    private final FileService fileService;
    private final ConsultationFileRepository consultationFileRepository;
    private final CasesService casesService;
    private final NotificationService notificationService;
    private final UserRepository userRepository;

    // Consultation Reservation Management Methods
    @Transactional
    public ConsultationReservationResponse createConsultationReservation(
            CreateConsultationReservationRequest request) {
        ConsultationReservation consultationReservation = request.toEntity();

        ConsultationReservation saved =
                consultationReservationRepository.save(consultationReservation);
        return ConsultationReservationResponse.from(saved);
    }

    public Page<ConsultationReservationResponse> getConsultationReservations(
            ConsultationRequestStatus status, Pageable pageable) {
        Page<ConsultationReservation> reservations;

        if (status != null) {
            reservations = consultationReservationRepository.findByStatus(status, pageable);
        } else {
            reservations = consultationReservationRepository.findAll(pageable);
        }

        return reservations.map(ConsultationReservationResponse::from);
    }

    public ConsultationReservationResponse getConsultationRequest(Long id) {
        ConsultationReservation consultationReservation =
                consultationReservationRepository
                        .findById(id)
                        .orElseThrow(
                                () ->
                                        new ConsultationException(
                                                ConsultationErrorCode
                                                        .CONSULTATION_RESERVATION_NOT_FOUND));
        ConsultationReservationResponse response =
                ConsultationReservationResponse.from(consultationReservation);

        // 해당 예약으로 생성된 상담이 있는지 확인
        consultationRepository
                .findByConsultationReservation_Id(id)
                .ifPresent(consultation -> response.setConsultationId(consultation.getId()));

        return response;
    }

    @Transactional
    public ConsultationReservationResponse approveConsultationReservation(
            Long id, ApproveConsultationReservation request) {
        ConsultationReservation consultationReservation =
                consultationReservationRepository
                        .findById(id)
                        .orElseThrow(
                                () ->
                                        new ConsultationException(
                                                ConsultationErrorCode
                                                        .CONSULTATION_RESERVATION_NOT_FOUND));

        User counselor = userService.getUser(request.getLawyerId());
        consultationReservation.approve(counselor);

        return ConsultationReservationResponse.from(consultationReservation);
    }

    @Transactional
    public ConsultationReservationResponse rejectConsultationReservation(
            Long id, RejectConsultationReservation request) {
        ConsultationReservation consultationReservation =
                consultationReservationRepository
                        .findById(id)
                        .orElseThrow(
                                () ->
                                        new ConsultationException(
                                                ConsultationErrorCode
                                                        .CONSULTATION_RESERVATION_NOT_FOUND));

        consultationReservation.reject(request.getRejectionReason());

        return ConsultationReservationResponse.from(consultationReservation);
    }

    // Consultation Management Methods
    @Transactional
    public ConsultationResponse createConsultation(CreateConsultationRequest request) {
        Client client = clientService.getClient(request.getClient());
        User counselor = userService.getUser(request.getCounselorId());

        // 이미 해당 예약에 대한 상담이 존재하는지 확인 (1차 체크)
        Optional<Consultation> existingConsultation =
                consultationRepository.findByConsultationReservation_Id(request.getReservationId());

        if (existingConsultation.isPresent()) {
            Consultation existing = existingConsultation.get();
            existing.update(client, counselor, request.getConsultationDate());
            existing.startConsultation();
            return ConsultationResponse.from(existing);
        }

        ConsultationReservation reservation =
                consultationReservationRepository
                        .findById(request.getReservationId())
                        .orElseThrow(
                                () ->
                                        new ConsultationException(
                                                ConsultationErrorCode
                                                        .CONSULTATION_RESERVATION_NOT_FOUND));

        if (!reservation.isApproved()) {
            throw new ConsultationException(
                    ConsultationErrorCode.CONSULTATION_RESERVATION_NOT_APPROVED);
        }

        Consultation consultation =
                Consultation.builder()
                        .consultationReservation(reservation)
                        .client(client)
                        .counselor(counselor)
                        .consultationDate(request.getConsultationDate())
                        .build();

        try {
            Consultation saved = consultationRepository.save(consultation);
            return ConsultationResponse.from(saved);
        } catch (DataIntegrityViolationException e) {
            // 동시 요청 등으로 인한 중복 발생 시, 기존 상담 조회하여 업데이트 후 반환
            Consultation existing =
                    consultationRepository
                            .findByConsultationReservation_Id(request.getReservationId())
                            .orElseThrow(
                                    () ->
                                            new ConsultationException(
                                                    ConsultationErrorCode.CONSULTATION_NOT_FOUND));
            existing.update(client, counselor, request.getConsultationDate());
            existing.startConsultation();
            return ConsultationResponse.from(existing);
        }
    }

    public Page<ConsultationResponse> getConsultations(
            ConsultationStatus status, Pageable pageable) {
        Page<Consultation> consultations;

        if (status != null) {
            consultations = consultationRepository.findByStatus(status, pageable);
        } else {
            consultations = consultationRepository.findAll(pageable);
        }

        return consultations.map(ConsultationResponse::from);
    }

    public ConsultationResponse getConsultation(Long id) {
        Consultation consultation =
                consultationRepository
                        .findById(id)
                        .orElseThrow(
                                () ->
                                        new ConsultationException(
                                                ConsultationErrorCode.CONSULTATION_NOT_FOUND));
        return ConsultationResponse.from(consultation);
    }

    @Transactional
    public ConsultationResponse startConsultation(Long id) {
        Consultation consultation =
                consultationRepository
                        .findById(id)
                        .orElseThrow(
                                () ->
                                        new ConsultationException(
                                                ConsultationErrorCode.CONSULTATION_NOT_FOUND));

        consultation.startConsultation();
        return ConsultationResponse.from(consultation);
    }

    @Transactional
    public ConsultationResponse completeConsultation(Long id) {
        Consultation consultation =
                consultationRepository
                        .findById(id)
                        .orElseThrow(
                                () ->
                                        new ConsultationException(
                                                ConsultationErrorCode.CONSULTATION_NOT_FOUND));

        consultation.completeConsultation();
        Cases createdCase = casesService.createCaseFromConsultation(consultation);

        // 알림 전송 (모든 사무관에게)
        List<User> secretaries = userRepository.findAllByRole(Role.ROLE_SECRETARY);
        String content = String.format("새로운 사건(미배정)이 생성되었습니다: %s", createdCase.getTitle());
        String url = "/cases/unassigned/" + createdCase.getId();

        for (User secretary : secretaries) {
            notificationService.send(secretary, NotificationType.CASE_CREATED, content, url);
        }

        return ConsultationResponse.from(consultation);
    }

    // Consultation Note Management Methods
    @Transactional
    public ConsultationNoteResponse createConsultationNote(
            Long consultationId, ConsultationNoteRequest request) {
        Consultation consultation =
                consultationRepository
                        .findById(consultationId)
                        .orElseThrow(
                                () ->
                                        new ConsultationException(
                                                ConsultationErrorCode.CONSULTATION_NOT_FOUND));

        if (!consultation.isInProgress()) {
            throw new ConsultationException(ConsultationErrorCode.CONSULTATION_NOT_IN_PROGRESS);
        }

        ConsultationNote consultationNote =
                request.toEntity(consultation, consultation.getCounselor());

        ConsultationNote saved = consultationNoteRepository.save(consultationNote);
        return ConsultationNoteResponse.from(saved);
    }

    public List<ConsultationNoteResponse> getConsultationNotes(Long consultationId) {
        return consultationNoteRepository
                .findByConsultationIdOrderByCreatedDateDesc(consultationId)
                .stream()
                .map(ConsultationNoteResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public ConsultationNoteResponse updateConsultationNote(
            Long consultationId, Long noteId, ConsultationNoteRequest request) {
        ConsultationNote consultationNote =
                consultationNoteRepository
                        .findById(noteId)
                        .orElseThrow(
                                () ->
                                        new ConsultationException(
                                                ConsultationErrorCode.CONSULTATION_NOTE_NOT_FOUND));

        if (!consultationNote.getConsultation().getId().equals(consultationId)) {
            throw new ConsultationException(ConsultationErrorCode.CONSULTATION_NOTE_NOT_FOUND);
        }

        if (!consultationNote.getConsultation().isInProgress()) {
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

        return ConsultationNoteResponse.from(consultationNote);
    }

    // File Management Methods
    @Transactional
    public String uploadConsultationFile(
            Long consultationId, MultipartFile file, String description) throws IOException {
        Consultation consultation =
                consultationRepository
                        .findById(consultationId)
                        .orElseThrow(
                                () ->
                                        new ConsultationException(
                                                ConsultationErrorCode.CONSULTATION_NOT_FOUND));

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

    public ConsultationFile getConsultationFileById(Long fileId) {
        return consultationFileRepository
                .findById(fileId)
                .orElseThrow(() -> new RuntimeException("파일을 찾을 수 없습니다."));
    }

    @Transactional
    public ConsultationResponse updateCounselor(Long consultationId, Long newCounselorId) {
        Consultation consultation =
                consultationRepository
                        .findById(consultationId)
                        .orElseThrow(
                                () ->
                                        new ConsultationException(
                                                ConsultationErrorCode.CONSULTATION_NOT_FOUND));

        User newCounselor = userService.getUser(newCounselorId);
        consultation.updateCounselor(newCounselor);

        return ConsultationResponse.from(consultation);
    }
}
