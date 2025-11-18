package com.haeil.be.cases.controller;

import com.haeil.be.cases.dto.request.AssignAttorneyRequest;
import com.haeil.be.cases.dto.request.DecisionRequest;
import com.haeil.be.cases.service.CasesService;
import com.haeil.be.global.response.ApiResponse;
import com.haeil.be.user.service.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Cases", description = "사건 관련 API")
@RequestMapping("/api/v1/cases")
@RestController
@RequiredArgsConstructor
public class CasesController {

    private final CasesService casesService;

    // 미배정 사건 목록조회
    @Operation(summary = "미배정 사건 목록 조회", description = "현재 배정되지 않은 사건들을 조회합니다.")
    @GetMapping("/unassigned")
    public ApiResponse<Object> getUnassignedCases() {
        return ApiResponse.from(casesService.getUnassignedCases());
    }

    // 미배정 사건 상세보기
    @Operation(summary = "미배정 사건 상세보기", description = "미배정 사건의 상세 내용을 조회합니다.")
    @GetMapping("/unassigned/{caseId}")
    public ApiResponse<Object> getUnassignedCaseDetail(@PathVariable Long caseId) {
        return ApiResponse.from(casesService.getUnassignedCaseDetail(caseId));
    }

    // 변호사 배정요청
    @Operation(summary = "변호사 배정 요청", description = "변호사 배정을 요청합니다.")
    @PostMapping("/unassigned/{caseId}/assign")
    public ApiResponse<Object> assignAttorney(
            @PathVariable Long caseId, @RequestBody AssignAttorneyRequest request) {
        casesService.assignAttorney(caseId, request);
        return ApiResponse.EMPTY_RESPONSE;
    }

    // 요청된 사건 목록조회
    @Operation(summary = "요청된 사건 목록 조회", description = "변호사가 자신의 사건 배정 요청 목록을 조회합니다.")
    @GetMapping("/requested")
    public ApiResponse<Object> getRequestedCases(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ApiResponse.from(casesService.getRequestedCases(userDetails.getId()));
    }

    // 요청된 사건 상세보기
    @Operation(summary = "요청된 사건 상세보기", description = "요청된 사건의 상세 내용을 조회합니다.")
    @GetMapping("/requested/{caseId}")
    public ApiResponse<Object> getRequestedCaseDetail(
            @PathVariable Long caseId, @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ApiResponse.from(casesService.getRequestedCaseDetail(caseId, userDetails.getId()));
    }

    // 사건 배정 승인/거절
    @Operation(summary = "사건배정 승인/거절", description = "요청받은 변호사가 사건 배정을 승인하거나 거절합니다.")
    @PatchMapping("/requested/{caseId}")
    public ApiResponse<Object> decideCaseAssignment(
            @PathVariable Long caseId,
            @RequestBody DecisionRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        casesService.decideCaseAssignment(caseId, request, userDetails.getId());
        return ApiResponse.EMPTY_RESPONSE;
    }

    // 진행중인 사건 목록조회
    @Operation(summary = "진행 중 사건 목록 조회", description = "담당 변호사가 맡은 진행 중 사건들을 조회합니다.")
    @GetMapping("/ongoing")
    public ApiResponse<Object> getOngoingCases(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ApiResponse.from(casesService.getOngoingCases(userDetails.getId()));
    }

    // 진행중인 사건 상세보기
    @Operation(summary = "진행 중 사건 상세보기", description = "진행 중 사건의 상세 정보를 조회합니다.")
    @GetMapping("/ongoing/{caseId}")
    public ApiResponse<Object> getOngoingCaseDetail(
            @PathVariable Long caseId, @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ApiResponse.from(casesService.getOngoingCaseDetail(caseId, userDetails.getId()));
    }
}
