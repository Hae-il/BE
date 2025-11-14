package com.haeil.be.contract.dto.request;

import com.haeil.be.contract.domain.type.ExpenseHandling;
import jakarta.validation.constraints.NotNull;

public record ExpenseInfoRequest(
        @NotNull ExpenseHandling expenseHandling,
        String expenseDetail
) {}
