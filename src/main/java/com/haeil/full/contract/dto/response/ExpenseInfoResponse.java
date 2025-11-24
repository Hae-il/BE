package com.haeil.full.contract.dto.response;

import com.haeil.full.contract.domain.ExpenseInfo;
import com.haeil.full.contract.domain.type.ExpenseHandling;

public record ExpenseInfoResponse(ExpenseHandling expenseHandling, String expenseDetail) {
    public static ExpenseInfoResponse from(ExpenseInfo expenseInfo) {
        return new ExpenseInfoResponse(
                expenseInfo.getExpenseHandling(), expenseInfo.getExpenseDetail());
    }
}
