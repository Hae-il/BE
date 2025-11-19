package com.haeil.be.settlement.dto.response;

import com.haeil.be.cases.domain.Cases;
import com.haeil.be.settlement.domain.Settlement;
import com.haeil.be.settlement.domain.type.PaymentStatus;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SettlementListResponse {

    private Long caseId;
    private String caseTitle;
    private Long settlementId;
    private PaymentStatus paymentStatus;
    private BigDecimal agreementAmount;
    private LocalDate paymentDueDate;

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
                .caseTitle(cases.getTitle())
                .settlementId(settlement != null ? settlement.getId() : null)
                .paymentStatus(settlement != null ? settlement.getPaymentStatus() : null)
                .agreementAmount(settlement != null ? settlement.getAgreementAmount() : null)
                .paymentDueDate(settlement != null ? settlement.getPaymentDueDate() : null)
                .build();
    }
}
