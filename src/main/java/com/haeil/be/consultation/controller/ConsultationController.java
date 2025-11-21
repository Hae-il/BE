package com.haeil.be.consultation.controller;

import com.haeil.be.consultation.domain.ConsultationFile;
import com.haeil.be.consultation.dto.request.ApproveConsultationReservation;
import com.haeil.be.consultation.dto.request.ConsultationNoteRequest;
import com.haeil.be.consultation.dto.request.CreateConsultationRequest;
import com.haeil.be.consultation.dto.request.CreateConsultationReservationRequest;
import com.haeil.be.consultation.dto.request.RejectConsultationReservation;
import com.haeil.be.consultation.dto.response.ConsultationNoteResponse;
import com.haeil.be.consultation.dto.response.ConsultationReservationResponse;
import com.haeil.be.consultation.dto.response.ConsultationResponse;
import com.haeil.be.consultation.service.ConsultationService;
import com.haeil.be.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Consultation", description = "상담 관련 API")
@RequestMapping("/api/v1/consultations")
@RestController
@RequiredArgsConstructor
public class ConsultationController {

    private final ConsultationService consultationService;

    @Operation(summary = "상담 예약 신청", description = "새로운 상담 예약을 신청합니다.")
    @PostMapping("/reservations")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<Object> createConsultationReservation(
            @Valid @RequestBody CreateConsultationReservationRequest request) {
        ConsultationReservationResponse response =
                consultationService.createConsultationReservation(request);
        return ApiResponse.from(response);
    }

    @Operation(summary = "상담 예약 목록 조회", description = "상담 예약 목록을 조회합니다.")
    @GetMapping("/reservations")
    public ApiResponse<Object> getConsultationReservations() {
        List<ConsultationReservationResponse> responses =
                consultationService.getConsultationReservations();
        return ApiResponse.from(responses);
    }

    @Operation(summary = "상담 예약 상세 조회", description = "상담 예약 상세 정보를 조회합니다.")
    @GetMapping("/reservations/{id}")
    public ApiResponse<Object> getConsultationRequest(@PathVariable Long id) {
        ConsultationReservationResponse response = consultationService.getConsultationRequest(id);
        return ApiResponse.from(response);
    }

    @Operation(summary = "상담 예약 승인", description = "상담 예약을 승인하고 담당 변호사를 배정합니다.")
    @PatchMapping("/reservations/{id}/approve")
    public ApiResponse<Object> approveConsultationReservation(
            @PathVariable Long id, @Valid @RequestBody ApproveConsultationReservation request) {
        ConsultationReservationResponse response =
                consultationService.approveConsultationReservation(id, request);
        return ApiResponse.from(response);
    }

    @Operation(summary = "상담 예약 거절", description = "상담 예약을 거절하고 거절 사유를 기록합니다.")
    @PatchMapping("/reservations/{id}/reject")
    public ApiResponse<Object> rejectConsultationReservation(
            @PathVariable Long id, @Valid @RequestBody RejectConsultationReservation request) {
        ConsultationReservationResponse response =
                consultationService.rejectConsultationReservation(id, request);
        return ApiResponse.from(response);
    }

    @Operation(summary = "상담 시작", description = "승인된 예약을 기반으로 상담을 시작합니다.")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<Object> createConsultation(
            @Valid @RequestBody CreateConsultationRequest request) {
        ConsultationResponse response = consultationService.createConsultation(request);
        return ApiResponse.from(response);
    }

    @Operation(summary = "상담 목록 조회", description = "상담 목록을 조회합니다.")
    @GetMapping
    public ApiResponse<Object> getConsultations() {
        List<ConsultationResponse> responses = consultationService.getConsultations();
        return ApiResponse.from(responses);
    }

    @Operation(summary = "상담 상세 조회", description = "상담 상세 정보를 조회합니다.")
    @GetMapping("/{id}")
    public ApiResponse<Object> getConsultation(@PathVariable Long id) {
        ConsultationResponse response = consultationService.getConsultation(id);
        return ApiResponse.from(response);
    }

    @Operation(summary = "상담 시작", description = "상담을 시작 상태로 변경합니다.")
    @PatchMapping("/{id}/start")
    public ApiResponse<Object> startConsultation(@PathVariable Long id) {
        ConsultationResponse response = consultationService.startConsultation(id);
        return ApiResponse.from(response);
    }

    @Operation(summary = "상담 완료", description = "상담을 완료 상태로 변경하고 사건을 생성합니다.")
    @PatchMapping("/{id}/complete")
    public ApiResponse<Object> completeConsultation(@PathVariable Long id) {
        ConsultationResponse response = consultationService.completeConsultation(id);
        return ApiResponse.from(response);
    }

    @Operation(summary = "상담 노트 작성", description = "상담 진행 중에 노트를 작성합니다.")
    @PostMapping("/{id}/notes")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<Object> createConsultationNote(
            @PathVariable Long id, @Valid @RequestBody ConsultationNoteRequest request) {
        ConsultationNoteResponse response = consultationService.createConsultationNote(id, request);
        return ApiResponse.from(response);
    }

    @Operation(summary = "상담 노트 조회", description = "특정 상담의 모든 노트를 조회합니다.")
    @GetMapping("/{id}/notes")
    public ApiResponse<Object> getConsultationNotes(@PathVariable Long id) {
        List<ConsultationNoteResponse> responses = consultationService.getConsultationNotes(id);
        return ApiResponse.from(responses);
    }

    @Operation(summary = "상담 노트 수정", description = "담당 변호사만 상담 노트를 수정할 수 있습니다.")
    @PatchMapping("/{consultationId}/notes/{noteId}")
    public ApiResponse<Object> updateConsultationNote(
            @PathVariable Long consultationId,
            @PathVariable Long noteId,
            @Valid @RequestBody ConsultationNoteRequest request) {
        ConsultationNoteResponse response =
                consultationService.updateConsultationNote(consultationId, noteId, request);
        return ApiResponse.from(response);
    }

    @Operation(summary = "상담 파일 업로드", description = "상담에 관련된 파일을 업로드합니다.")
    @PostMapping("/{id}/files")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<Object> uploadConsultationFile(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "description", required = false) String description)
            throws IOException {
        String response = consultationService.uploadConsultationFile(id, file, description);
        return ApiResponse.from(response);
    }

    @Operation(summary = "상담 파일 목록 조회", description = "상담에 업로드된 파일 목록을 조회합니다.")
    @GetMapping("/{id}/files")
    public ApiResponse<Object> getConsultationFiles(@PathVariable Long id) {
        List<ConsultationFile> files = consultationService.getConsultationFiles(id);
        return ApiResponse.from(files);
    }

    @Operation(summary = "상담 담당자 변경", description = "진행 중인 상담의 담당 상담사를 변경합니다.")
    @PatchMapping("/{consultationId}/counselor")
    public ApiResponse<Object> updateCounselor(
            @PathVariable Long consultationId,
            @RequestParam Long newCounselorId) {
        ConsultationResponse response = consultationService.updateCounselor(consultationId, newCounselorId);
        return ApiResponse.from(response);
    }
}
