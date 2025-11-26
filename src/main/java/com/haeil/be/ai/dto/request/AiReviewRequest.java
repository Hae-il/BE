package com.haeil.be.ai.dto.request;

import jakarta.validation.constraints.NotBlank;

public record AiReviewRequest(
    @NotBlank(message = "검토할 소장 내용은 필수입니다.")
    String content
) {}

