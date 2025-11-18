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

    @NotNull(message = "변호사 수임료는 필수 입력 항목입니다.")
    private BigDecimal lawyerFee;

    @NotNull(message = "총액은 필수 입력 항목입니다.")
    private BigDecimal total;

    @NotNull(message = "경비는 필수 입력 항목입니다.")
    private BigDecimal expenses;

    private Boolean isVatIncluded = false;

    @NotNull(message = "의뢰인 미수금은 필수 입력 항목입니다.")
    private BigDecimal clientReceivable;

    private LocalDate settlementDate;

    @NotNull(message = "만료일은 필수 입력 항목입니다.")
    private LocalDate dueDate;

    private String note;

    @NotNull(message = "사건 ID는 필수 입력 항목입니다.")
    private Long caseId;

    @Builder
    public CreateSettlementRequest(
            BigDecimal lawyerFee,
            BigDecimal total,
            BigDecimal expenses,
            Boolean isVatIncluded,
            BigDecimal clientReceivable,
            LocalDate settlementDate,
            LocalDate dueDate,
            String note,
            Long caseId) {
        this.lawyerFee = lawyerFee;
        this.total = total;
        this.expenses = expenses;
        this.isVatIncluded = isVatIncluded;
        this.clientReceivable = clientReceivable;
        this.settlementDate = settlementDate;
        this.dueDate = dueDate;
        this.note = note;
        this.caseId = caseId;
    }
}
