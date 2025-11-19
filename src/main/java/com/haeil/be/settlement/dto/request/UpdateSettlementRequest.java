package com.haeil.be.settlement.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateSettlementRequest {

    private BigDecimal attorneyFee;
    private BigDecimal agreementAmount;
    private BigDecimal expenses;
    private Boolean isVatIncluded;
    private BigDecimal clientReceivable;
    private LocalDate settlementDate;
    private LocalDate paymentDueDate;
    private String note;

    @Builder
    public UpdateSettlementRequest(
            BigDecimal attorneyFee,
            BigDecimal agreementAmount,
            BigDecimal expenses,
            Boolean isVatIncluded,
            BigDecimal clientReceivable,
            LocalDate settlementDate,
            LocalDate paymentDueDate,
            String note) {
        this.attorneyFee = attorneyFee;
        this.agreementAmount = agreementAmount;
        this.expenses = expenses;
        this.isVatIncluded = isVatIncluded;
        this.clientReceivable = clientReceivable;
        this.settlementDate = settlementDate;
        this.paymentDueDate = paymentDueDate;
        this.note = note;
    }
}
