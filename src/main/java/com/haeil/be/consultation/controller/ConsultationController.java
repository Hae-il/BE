package com.haeil.be.consultation.controller;

import com.haeil.be.consultation.dto.request.CreateConsultationRequestDto;
import com.haeil.be.consultation.dto.request.UpdateConsultationRequestStatusDto;
import com.haeil.be.consultation.dto.response.ConsultationRequestResponse;
import com.haeil.be.consultation.service.ConsultationService;
import com.haeil.be.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

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
}
