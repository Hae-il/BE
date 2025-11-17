package com.haeil.be.settlement.dto.response;

import com.haeil.be.settlement.domain.Settlement;
import com.haeil.be.settlement.domain.type.PaymentStatus;
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
    private BigDecimal lawyerFee;
    private BigDecimal total;
    private BigDecimal expenses;
    private Boolean isVatIncluded;
    private BigDecimal clientReceivable;
    private LocalDate settlementDate;
    private LocalDate dueDate;
    private String note;
    private Long caseId;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public static SettlementResponse from(Settlement settlement) {
        return SettlementResponse.builder()
                .id(settlement.getId())
                .paymentStatus(settlement.getPaymentStatus())
                .lawyerFee(settlement.getLawyerFee())
                .total(settlement.getTotal())
                .expenses(settlement.getExpenses())
                .isVatIncluded(settlement.getIsVatIncluded())
                .clientReceivable(settlement.getClientReceivable())
                .settlementDate(settlement.getSettlementDate())
                .dueDate(settlement.getDueDate())
                .note(settlement.getNote())
                .caseId(
                        settlement.getRelatedCase() != null
                                ? settlement.getRelatedCase().getId()
                                : null)
                .createdDate(settlement.getCreatedDate())
                .modifiedDate(settlement.getModifiedDate())
                .build();
    }
}
