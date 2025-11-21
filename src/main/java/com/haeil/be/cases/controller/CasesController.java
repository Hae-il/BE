package com.haeil.be.cases.controller;

import com.haeil.be.cases.dto.request.AssignAttorneyRequest;
import com.haeil.be.cases.dto.request.CaseDocumentRequest;
import com.haeil.be.cases.dto.request.CaseNumberRequest;
import com.haeil.be.cases.dto.request.DecisionRequest;
import com.haeil.be.cases.dto.request.PetitionRequest;
import com.haeil.be.cases.service.CasesService;
import com.haeil.be.global.response.ApiResponse;
import com.haeil.be.user.service.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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

    // 소장 작성
    @Operation(summary = "소장 작성", description = "진행 중인 사건에 대한 소장을 작성합니다.")
    @PostMapping("/ongoing/{caseId}/petition")
    public ApiResponse<Object> createPetition(
            @PathVariable Long caseId,
            @RequestBody PetitionRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        casesService.createPetition(caseId, request, userDetails.getId());
        return ApiResponse.EMPTY_RESPONSE;
    }

    // 소장 조회
    @Operation(summary = "소장 조회", description = "작성된 소장을 조회합니다.")
    @GetMapping("/ongoing/{caseId}/petition")
    public ApiResponse<Object> getPetition(
            @PathVariable Long caseId, @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ApiResponse.from(casesService.getPetition(caseId, userDetails.getId()));
    }

    // 소장 수정
    @Operation(summary = "소장 수정", description = "작성된 소장을 수정합니다.")
    @PatchMapping("/ongoing/{caseId}/petition")
    public ApiResponse<Object> updatePetition(
            @PathVariable Long caseId,
            @RequestBody PetitionRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        casesService.updatePetition(caseId, request, userDetails.getId());
        return ApiResponse.EMPTY_RESPONSE;
    }

    // 법원에서 할당된 사건번호 추가
    @Operation(summary = "사건번호 등록", description = "정식 사건 번호를 추가합니다")
    @PatchMapping("/ongoing/{caseId}/caseNumber")
    public ApiResponse<Object> updateCaseNumber(
            @PathVariable Long caseId,
            @RequestBody CaseNumberRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        casesService.updateCaseNumber(caseId, request.caseNumber(), userDetails.getId());
        return ApiResponse.EMPTY_RESPONSE;
    }

    // 소송문서 업로드
    @Operation(summary = "소송문서 업로드", description = "진행 중인 사건에 소송문서를 업로드합니다.")
    @PostMapping("/ongoing/{caseId}/documents")
    public ApiResponse<Object> uploadCaseDocument(
            @PathVariable Long caseId,
            @RequestPart("file") MultipartFile file,
            @RequestPart("request") CaseDocumentRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails)
            throws java.io.IOException {
        return ApiResponse.from(
                casesService.uploadCaseDocument(caseId, file, request, userDetails.getId()));
    }

    // 소송문서 목록 조회
    @Operation(summary = "소송문서 목록 조회", description = "업로드된 소송문서 목록을 조회합니다.")
    @GetMapping("/ongoing/{caseId}/documents")
    public ApiResponse<Object> getCaseDocuments(
            @PathVariable Long caseId, @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ApiResponse.from(casesService.getCaseDocuments(caseId, userDetails.getId()));
    }

    // 소송문서 삭제
    @Operation(summary = "소송문서 삭제", description = "업로드된 소송문서를 삭제합니다.")
    @DeleteMapping("/ongoing/{caseId}/documents/{documentId}")
    public ApiResponse<Object> deleteCaseDocument(
            @PathVariable Long caseId,
            @PathVariable Long documentId,
            @AuthenticationPrincipal CustomUserDetails userDetails)
            throws java.io.IOException {
        casesService.deleteCaseDocument(caseId, documentId, userDetails.getId());
        return ApiResponse.EMPTY_RESPONSE;
    }

    // 사건 완료처리
    @Operation(summary = "사건 완료 처리", description = "진행 중 사건을 완료된 사건 상태로 변경합니다.")
    @PatchMapping("/ongoing/{caseId}/complete")
    public ApiResponse<Object> completeCase(
            @PathVariable Long caseId, @AuthenticationPrincipal CustomUserDetails userDetails) {

        casesService.completeCase(caseId, userDetails.getId());
        return ApiResponse.EMPTY_RESPONSE;
    }

    // 완료된 사건 목록조회
    @Operation(summary = "완료된 사건 목록 조회", description = "담당 변호사가 완료한 사건들을 조회합니다.")
    @GetMapping("/completed")
    public ApiResponse<Object> getCompletedCases(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ApiResponse.from(casesService.getCompletedCases(userDetails.getId()));
    }

    // 완료된 사건 상세보기
    @Operation(summary = "완료된 사건 상세보기", description = "완료된 사건의 상세 정보를 조회합니다.")
    @GetMapping("/completed/{caseId}")
    public ApiResponse<Object> getCompletedCaseDetail(
            @PathVariable Long caseId, @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ApiResponse.from(casesService.getCompletedCaseDetail(caseId, userDetails.getId()));
    }
}
