package com.haeil.be.contract.dto.request;

import com.haeil.be.contract.domain.type.FeeType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record ContractCreateRequest(
        @NotNull FeeType feeType,
        @NotNull Long caseId,
        @NotNull LocalDate dueDate,

        @NotNull @Valid ExpenseInfoRequest expenseInfoRequest,

        // 정액 계약일 경우
        @Valid List<ContractConditionRequest> contractConditionRequestList,

        @Min(value=0) BigDecimal targetAmount,
        @Min(value=0) Long feePercentage
) {
}
