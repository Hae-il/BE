package com.haeil.be.ai.controller;

import com.haeil.be.ai.dto.request.AiPetitionRequest;
import com.haeil.be.ai.service.AiPetitionService;
import com.haeil.be.cases.dto.response.PetitionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "소장 초안 작성 AI", description = "AI 관련 API")
@RestController
@RequestMapping("/api/v1/ai")
@RequiredArgsConstructor
public class AiPetitionController {

    private final AiPetitionService aiPetitionService;

    @Operation(summary = "소장 초안 생성", description = "AI를 이용하여 소장 초안을 생성합니다.")
    @PostMapping("/petition/draft")
    public ResponseEntity<PetitionResponse> generatePetitionDraft(
            @Valid @RequestBody AiPetitionRequest request) {
        PetitionResponse response = aiPetitionService.generatePetitionDraft(request.getCaseId());
        return ResponseEntity.ok(response);
    }
}
