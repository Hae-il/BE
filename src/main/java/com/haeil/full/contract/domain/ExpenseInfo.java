package com.haeil.full.contract.domain;

import com.haeil.full.contract.domain.type.ExpenseHandling;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
public class ExpenseInfo {

    @Column(name = "expense_handling")
    @Enumerated(EnumType.STRING)
    private ExpenseHandling expenseHandling;

    @Column(name = "expense_detail")
    private String expenseDetail;

    public ExpenseInfo(ExpenseHandling expenseHandling, String expenseDetail) {
        this.expenseHandling = expenseHandling;
        this.expenseDetail = expenseDetail;
    }
}
