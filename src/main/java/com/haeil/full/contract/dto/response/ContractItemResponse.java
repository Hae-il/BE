package com.haeil.full.contract.dto.response;

import com.haeil.full.cases.domain.Cases;
import com.haeil.full.contract.domain.Contract;
import com.haeil.full.contract.domain.type.ContractStatus;

public record ContractItemResponse(
        Long caseId,
        Long contractId,
        String caseNumber,
        String clientName,
        String attorneyName,
        ContractStatus status,
        String dueDate) {

    public static ContractItemResponse from(Cases cases) {
        Contract contract = cases.getContract();
        Long contractId = (contract != null) ? contract.getId() : null;
        ContractStatus status = (contract != null) ? contract.getStatus() : null;

        String dueDate =
                (contract != null && contract.getDueDate() != null)
                        ? contract.getDueDate().toString()
                        : "-";

        String clientName = (cases.getClient() != null) ? cases.getClient().getName() : "-";
        String attorneyName = (cases.getAttorney() != null) ? cases.getAttorney().getName() : "-";

        // 사건 관리와 동일하게 C + ID 형식으로 표시
        String caseNumber = "C" + cases.getId();

        return new ContractItemResponse(
                cases.getId(), contractId, caseNumber, clientName, attorneyName, status, dueDate);
    }
}
