package com.haeil.full.settlement.dto.response;

import com.haeil.full.settlement.domain.Settlement;
import com.haeil.full.settlement.domain.type.PaymentStatus;
import com.haeil.full.settlement.domain.type.SettlementStatus;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SettlementResponse {

    private Long id;
    private PaymentStatus paymentStatus;
    private SettlementStatus settlementStatus;
    private BigDecimal attorneyFee;
    private BigDecimal agreementAmount;
    private BigDecimal expenses;
    private Boolean isVatIncluded;
    private BigDecimal clientReceivable;
    private LocalDate settlementDate;
    private LocalDate paymentDueDate;
    private String note;
    private Long caseId;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public static SettlementResponse from(Settlement settlement) {
        return SettlementResponse.builder()
                .id(settlement.getId())
                .paymentStatus(settlement.getPaymentStatus())
                .settlementStatus(settlement.getSettlementStatus())
                .attorneyFee(settlement.getAttorneyFee())
                .agreementAmount(settlement.getAgreementAmount())
                .expenses(settlement.getExpenses())
                .isVatIncluded(settlement.getIsVatIncluded())
                .clientReceivable(settlement.getClientReceivable())
                .settlementDate(settlement.getSettlementDate())
                .paymentDueDate(settlement.getPaymentDueDate())
                .note(settlement.getNote())
                .caseId(settlement.getCases() != null ? settlement.getCases().getId() : null)
                .createdDate(settlement.getCreatedDate())
                .modifiedDate(settlement.getModifiedDate())
                .build();
    }
}
