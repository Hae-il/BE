package com.haeil.full.settlement.dto.request;

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
public class UpdateSettlementRequest {

    private BigDecimal attorneyFee;
    private BigDecimal agreementAmount;
    private BigDecimal expenses;
    private Boolean isVatIncluded;
    private LocalDate settlementDate;
    private LocalDate paymentDueDate;
    private String note;
}
