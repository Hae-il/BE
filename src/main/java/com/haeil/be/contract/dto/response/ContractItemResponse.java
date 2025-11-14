package com.haeil.be.contract.dto.response;

import com.haeil.be.contract.domain.Contract;
import com.haeil.be.contract.domain.type.ContractStatus;
import java.time.LocalDate;

public record ContractItemResponse(
        Long contractId, String caseNumber, ContractStatus status, LocalDate dueDate) {
    // String attorneyName랑 String clientName가 포함되어야 하는데, 지금 Case랑 Consultation이 매핑이 안되어 있어서 일단
    // 제외했습니다.

    public static ContractItemResponse from(Contract contract) {
        return new ContractItemResponse(
                contract.getId(),
                contract.getCases().getCaseNumber(),
                contract.getStatus(),
                contract.getDueDate());
    }
}
