package com.haeil.full.contract.dto.request;

import com.haeil.full.contract.domain.type.ExpenseHandling;
import jakarta.validation.constraints.NotNull;

public record ExpenseInfoRequest(@NotNull ExpenseHandling expenseHandling, String expenseDetail) {}
