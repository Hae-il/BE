package com.haeil.full.contract.dto.response;

import com.haeil.full.contract.domain.ContractCondition;
import java.math.BigDecimal;

public record ContractConditionResponse(
        Long conditionId, String conditionDetail, BigDecimal amount) {
    public static ContractConditionResponse from(ContractCondition condition) {
        return new ContractConditionResponse(
                condition.getId(), condition.getConditionDetail(), condition.getAmount());
    }
}
