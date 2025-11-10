package com.haeil.be.consultation.controller;

import com.haeil.be.consultation.domain.ConsultationFile;
import com.haeil.be.consultation.dto.request.CreateConsultationDto;
import com.haeil.be.consultation.dto.request.CreateConsultationNoteDto;
import com.haeil.be.consultation.dto.request.CreateConsultationRequestDto;
import com.haeil.be.consultation.dto.request.UpdateConsultationNoteDto;
import com.haeil.be.consultation.dto.request.UpdateConsultationRequestStatusDto;
import com.haeil.be.consultation.dto.request.UpdateConsultationStatusDto;
import com.haeil.be.consultation.dto.response.ConsultationNoteResponse;
import com.haeil.be.consultation.dto.response.ConsultationRequestResponse;
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
    @PostMapping("/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<Object> createConsultationRequest(
            @Valid @RequestBody CreateConsultationRequestDto request) {
        ConsultationRequestResponse response =
                consultationService.createConsultationRequest(request);
        return ApiResponse.from(response);
    }

    @Operation(summary = "상담 예약 목록 조회", description = "상담 예약 목록을 조회합니다.")
    @GetMapping("/requests")
    public ApiResponse<Object> getConsultationRequests() {
        List<ConsultationRequestResponse> responses = consultationService.getConsultationRequests();
        return ApiResponse.from(responses);
    }

    @Operation(summary = "상담 예약 상세 조회", description = "상담 예약 상세 정보를 조회합니다.")
    @GetMapping("/requests/{id}")
    public ApiResponse<Object> getConsultationRequest(@PathVariable Long id) {
        ConsultationRequestResponse response = consultationService.getConsultationRequest(id);
        return ApiResponse.from(response);
    }

    @Operation(summary = "상담 예약 상태 변경", description = "상담 예약 상태를 승인 또는 거절로 변경합니다.")
    @PatchMapping("/requests/{id}")
    public ApiResponse<Object> updateConsultationRequestStatus(
            @PathVariable Long id, @Valid @RequestBody UpdateConsultationRequestStatusDto request) {
        ConsultationRequestResponse response =
                consultationService.updateConsultationRequestStatus(id, request);
        return ApiResponse.from(response);
    }

    // Consultation Management Endpoints
    @Operation(summary = "상담 시작", description = "승인된 예약을 기반으로 상담을 시작합니다.")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<Object> createConsultation(
            @Valid @RequestBody CreateConsultationDto request) {
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

    @Operation(summary = "상담 상태 변경", description = "상담 진행 상태를 변경합니다.")
    @PatchMapping("/{id}")
    public ApiResponse<Object> updateConsultationStatus(
            @PathVariable Long id, @Valid @RequestBody UpdateConsultationStatusDto request) {
        ConsultationResponse response = consultationService.updateConsultationStatus(id, request);
        return ApiResponse.from(response);
    }

    // ConsultationNote Management Endpoints
    @Operation(summary = "상담 노트 작성", description = "상담 진행 중에 노트를 작성합니다.")
    @PostMapping("/{id}/notes")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<Object> createConsultationNote(
            @PathVariable Long id, @Valid @RequestBody CreateConsultationNoteDto request) {
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
            @Valid @RequestBody UpdateConsultationNoteDto request) {
        ConsultationNoteResponse response =
                consultationService.updateConsultationNote(consultationId, noteId, request);
        return ApiResponse.from(response);
    }

    @Operation(summary = "상담 파일 업로드", description = "상담에 관련된 파일을 업로드합니다.")
    @PostMapping("/{consultationId}/files")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<Object> uploadConsultationFile(
            @PathVariable Long consultationId,
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "description", required = false) String description)
            throws IOException {
        String response =
                consultationService.uploadConsultationFile(consultationId, file, description);
        return ApiResponse.from(response);
    }

    @Operation(summary = "상담 파일 목록 조회", description = "상담에 업로드된 파일 목록을 조회합니다.")
    @GetMapping("/{consultationId}/files")
    public ApiResponse<Object> getConsultationFiles(@PathVariable Long consultationId) {
        List<ConsultationFile> files = consultationService.getConsultationFiles(consultationId);
        return ApiResponse.from(files);
    }
}
