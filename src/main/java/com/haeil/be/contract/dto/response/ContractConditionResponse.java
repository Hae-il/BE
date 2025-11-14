package com.haeil.be.contract.dto.response;

import com.haeil.be.contract.domain.ContractCondition;
import com.haeil.be.contract.domain.type.ExpenseHandling;

import java.math.BigDecimal;

public record ContractConditionResponse(
        Long conditionId,
        String conditionDetail,
        BigDecimal amount
) {
    public static ContractConditionResponse from(ContractCondition condition) {
        return new ContractConditionResponse(
                condition.getId(),
                condition.getConditionDetail(),
                condition.getAmount()
        );
    }
}