package com.haeil.be.consultation.service;

import com.haeil.be.consultation.domain.ConsultationRequest;
import com.haeil.be.consultation.domain.type.ConsultationRequestStatus;
import com.haeil.be.consultation.dto.request.CreateConsultationRequestDto;
import com.haeil.be.consultation.dto.request.UpdateConsultationRequestStatusDto;
import com.haeil.be.consultation.dto.response.ConsultationRequestResponse;
import com.haeil.be.consultation.exception.ConsultationException;
import com.haeil.be.consultation.exception.errorcode.ConsultationErrorCode;
import com.haeil.be.consultation.repository.ConsultationRequestRepository;
import com.haeil.be.user.domain.User;
import com.haeil.be.user.repository.UserRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ConsultationService {

    private final ConsultationRequestRepository consultationRequestRepository;
    private final UserRepository userRepository;

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
}
