package com.haeil.be.contract.dto.response;

import com.haeil.be.contract.domain.Contract;
import java.time.LocalDate;

public record ContractResponse(
        Long contractId,
        Long caseId,
        LocalDate dueDate
) {
    public static ContractResponse from(Contract contract){
        return new ContractResponse(
                contract.getId(),
                contract.getCases().getId(),
                contract.getDueDate());
    }
}
