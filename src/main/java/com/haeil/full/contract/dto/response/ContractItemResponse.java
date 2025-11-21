package com.haeil.full.contract.dto.response;

import com.haeil.full.contract.domain.Contract;
import com.haeil.full.contract.domain.type.ContractStatus;
import java.time.LocalDate;

public record ContractItemResponse(
        Long contractId,
        String caseNumber,
        String clientName,
        String attorneyName,
        ContractStatus status,
        LocalDate dueDate) {

    public static ContractItemResponse from(Contract contract) {
        return new ContractItemResponse(
                contract.getId(),
                contract.getCases().getCaseNumber(),
                contract.getCases().getClient().getName(),
                contract.getCases().getAttorney().getName(),
                contract.getStatus(),
                contract.getDueDate());
    }
}
