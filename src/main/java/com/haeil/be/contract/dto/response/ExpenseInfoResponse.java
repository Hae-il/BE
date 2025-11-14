package com.haeil.be.contract.dto.response;

import com.haeil.be.contract.domain.ExpenseInfo;
import com.haeil.be.contract.domain.type.ExpenseHandling;

public record ExpenseInfoResponse(ExpenseHandling expenseHandling, String expenseDetail) {
    public static ExpenseInfoResponse from(ExpenseInfo expenseInfo) {
        return new ExpenseInfoResponse(
                expenseInfo.getExpenseHandling(), expenseInfo.getExpenseDetail());
    }
}
