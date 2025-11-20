package com.haeil.be.settlement.dto.request;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateSettlementRequest {

    private BigDecimal attorneyFee;

    private BigDecimal agreementAmount;

    private BigDecimal expenses;

    private Boolean isVatIncluded = false;

    private LocalDate settlementDate;

    private LocalDate paymentDueDate;

    private String note;

    @NotNull(message = "사건 ID는 필수 입력 항목입니다.")
    private Long caseId;

    @Builder
    public CreateSettlementRequest(
            BigDecimal attorneyFee,
            BigDecimal agreementAmount,
            BigDecimal expenses,
            Boolean isVatIncluded,
            LocalDate settlementDate,
            LocalDate paymentDueDate,
            String note,
            Long caseId) {
        this.attorneyFee = attorneyFee;
        this.agreementAmount = agreementAmount;
        this.expenses = expenses;
        this.isVatIncluded = isVatIncluded;
        this.settlementDate = settlementDate;
        this.paymentDueDate = paymentDueDate;
        this.note = note;
        this.caseId = caseId;
    }
}
