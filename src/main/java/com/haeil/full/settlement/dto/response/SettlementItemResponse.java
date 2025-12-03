package com.haeil.full.settlement.dto.response;

import com.haeil.full.cases.domain.Cases;
import com.haeil.full.settlement.domain.Settlement;
import com.haeil.full.settlement.domain.type.SettlementStatus;

public record SettlementItemResponse(
        Long caseId,
        Long settlementId,
        String caseNumber,
        String clientName,
        String attorneyName,
        SettlementStatus status,
        String paymentDueDate) {

    public static SettlementItemResponse from(Cases cases) {
        Settlement settlement = cases.getSettlement();
        Long settlementId = (settlement != null) ? settlement.getId() : null;
        SettlementStatus status = (settlement != null) ? settlement.getSettlementStatus() : null;

        String paymentDueDate =
                (settlement != null && settlement.getPaymentDueDate() != null)
                        ? settlement.getPaymentDueDate().toString()
                        : "-";

        String clientName = (cases.getClient() != null) ? cases.getClient().getName() : "-";
        String attorneyName = (cases.getAttorney() != null) ? cases.getAttorney().getName() : "-";

        // 사건 관리와 동일하게 C + ID 형식으로 표시
        String caseNumber = "C" + cases.getId();

        return new SettlementItemResponse(
                cases.getId(),
                settlementId,
                caseNumber,
                clientName,
                attorneyName,
                status,
                paymentDueDate);
    }
}
