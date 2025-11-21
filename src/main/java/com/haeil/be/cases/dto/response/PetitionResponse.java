package com.haeil.be.cases.dto.response;

import com.haeil.be.cases.domain.Petition;

public record PetitionResponse(
        Long petitionId,
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
        // 첨부 서류
        String attachedDocuments) {
    public static PetitionResponse from(Petition petition) {
        return new PetitionResponse(
                petition.getId(),
                petition.getClaimAmount(),
                petition.getClaimContent(),
                petition.getAccidentCircumstances(),
                petition.getDamageItems(),
                petition.getDamageCalculation(),
                petition.getLiabilityBasis(),
                petition.getProofMethod(),
                petition.getAttachedDocuments());
    }
}
