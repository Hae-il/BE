package com.haeil.be.settlement.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateSettlementRequest {

    private BigDecimal lawyerFee;
    private BigDecimal total;
    private BigDecimal expenses;
    private Boolean isVatIncluded;
    private BigDecimal clientReceivable;
    private LocalDate settlementDate;
    private LocalDate dueDate;
    private String note;

    @Builder
    public UpdateSettlementRequest(
            BigDecimal lawyerFee,
            BigDecimal total,
            BigDecimal expenses,
            Boolean isVatIncluded,
            BigDecimal clientReceivable,
            LocalDate settlementDate,
            LocalDate dueDate,
            String note) {
        this.lawyerFee = lawyerFee;
        this.total = total;
        this.expenses = expenses;
        this.isVatIncluded = isVatIncluded;
        this.clientReceivable = clientReceivable;
        this.settlementDate = settlementDate;
        this.dueDate = dueDate;
        this.note = note;
    }
}
