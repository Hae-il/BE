package com.haeil.be.ai.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AiPetitionRequest {

    @Schema(description = "사건 ID", example = "1")
    @NotNull(message = "사건 ID는 필수입니다.")
    private Long caseId;
}
