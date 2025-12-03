package com.haeil.full.settlement.dto.response;

import com.haeil.full.cases.domain.Cases;
import com.haeil.full.settlement.domain.Settlement;
import com.haeil.full.settlement.domain.type.PaymentStatus;
import com.haeil.full.settlement.domain.type.SettlementStatus;
import java.math.BigDecimal;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SettlementListResponse {

    private Long caseId;
    private String caseNumber;
    private String clientName;
    private String attorneyName;
    private Long settlementId;
    private PaymentStatus paymentStatus;
    private SettlementStatus settlementStatus;
    private BigDecimal agreementAmount;
    private String paymentDueDate;

    /**
     * Repository에서 반환된 Object[] 결과를 SettlementListResponse로 변환합니다. Object[]의 첫 번째 요소는 Cases, 두 번째
     * 요소는 Settlement (또는 null)입니다.
     *
     * @param result Object[] 배열 [Cases, Settlement]
     * @return SettlementListResponse
     */
    public static SettlementListResponse from(Object[] result) {
        Cases cases = (Cases) result[0];
        Settlement settlement = (Settlement) result[1];

        return SettlementListResponse.builder()
                .caseId(cases.getId())
                .caseNumber("C" + cases.getId())
                .clientName(cases.getClient() != null ? cases.getClient().getName() : "-")
                .attorneyName(cases.getAttorney() != null ? cases.getAttorney().getName() : "-")
                .settlementId(settlement != null ? settlement.getId() : null)
                .paymentStatus(settlement != null ? settlement.getPaymentStatus() : null)
                .settlementStatus(settlement != null ? settlement.getSettlementStatus() : null)
                .agreementAmount(settlement != null ? settlement.getAgreementAmount() : null)
                .paymentDueDate(
                        settlement != null && settlement.getPaymentDueDate() != null
                                ? settlement.getPaymentDueDate().toString()
                                : "-")
                .build();
    }
}
