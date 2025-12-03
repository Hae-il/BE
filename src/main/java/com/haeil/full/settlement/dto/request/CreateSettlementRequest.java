package com.haeil.full.settlement.dto.request;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateSettlementRequest {

    private BigDecimal attorneyFee;

    private BigDecimal agreementAmount;

    private BigDecimal expenses;

    @Builder.Default private Boolean isVatIncluded = false;

    private LocalDate settlementDate;

    private LocalDate paymentDueDate;

    private String note;

    @NotNull(message = "사건 ID는 필수 입력 항목입니다.")
    private Long caseId;
}
