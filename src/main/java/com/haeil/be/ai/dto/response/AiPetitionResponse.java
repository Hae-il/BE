package com.haeil.be.ai.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AiPetitionResponse {

    @Schema(description = "청구금액", example = "10000000")
    private Long claimAmount;

    @Schema(description = "청구내용", example = "피고는 원고에게 금 10,000,000원 및 이에 대한 지연손해금을 지급하라.")
    private String claimContent;

    @Schema(description = "사고경위", example = "2024년 1월 1일 서울 강남구에서 발생한 교통사고...")
    private String accidentCircumstances;

    @Schema(description = "손해항목", example = "1. 치료비 2. 위자료")
    private String damageItems;

    @Schema(description = "손해액 산정", example = "치료비: 5,000,000원, 위자료: 5,000,000원")
    private String damageCalculation;

    @Schema(description = "책임 인정 근거", example = "민법 제750조 불법행위 책임")
    private String liabilityBasis;

    @Schema(description = "입증방법", example = "1. 교통사고사실확인원 2. 진단서")
    private String proofMethod;

    @Schema(description = "첨부서류", example = "1. 소장 부본 2. 증거자료")
    private String attachedDocuments;
}
