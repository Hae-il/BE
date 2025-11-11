package com.haeil.be.cases.controller;

import com.haeil.be.cases.dto.request.AssignAttorneyRequest;
import com.haeil.be.cases.service.CasesService;
import com.haeil.be.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
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

    //미배정 사건 목록조회
    @GetMapping("/unassigned")
    public ApiResponse<Object> getUnassignedCases() {
        return ApiResponse.from(casesService.getUnassignedCases());
    }

    //미배정 사건 상세보기
    @GetMapping("/unassigned/{caseId}")
    public ApiResponse<Object> getUnassignedCaseDetail(@PathVariable Long caseId) {
        return ApiResponse.from(casesService.getUnassignedCaseDetail(caseId));
    }

    //변호사 배정요청
    @PostMapping("/unassigned/{caseId}/assign")
    public ApiResponse<Object> assignAttorney(
            @PathVariable Long caseId, @RequestBody AssignAttorneyRequest request) {
        casesService.assignAttorney(caseId, request);
        return ApiResponse.EMPTY_RESPONSE;
    }
}
