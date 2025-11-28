package com.haeil.be.ai.controller;

import com.haeil.be.ai.service.AiReviewService;
import com.haeil.be.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "AI Legal Review", description = "AI 법률 검토 API")
@RestController
@RequestMapping("/api/v1/ai")
@RequiredArgsConstructor
public class AiReviewController {

    private final AiReviewService aiReviewService;

    @Operation(summary = "소장 자동 검토 (Case ID)", description = "저장된 소장 데이터를 불러와 AI가 검토합니다.")
    @PostMapping("/review/{caseId}")
    public ApiResponse<Object> reviewPetitionByCaseId(@PathVariable Long caseId) {
        String result = aiReviewService.reviewPetition(caseId);
        return ApiResponse.from(result);
    }
}

