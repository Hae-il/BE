package com.haeil.be.cases.dto.request;

public record PetitionRequest(
        // 청구 취지
        Long claimAmount,
        String claimContent,
        // 청구 원인
        String accidentCircumstances,
        String damageItems,
        String damageCalculation,
        String liabilityBasis,
        // 입증 방법
        String proofMethod,
        // 첨부 서류(일단 string으로 처리했습니다!)
        String attachedDocuments) {}
