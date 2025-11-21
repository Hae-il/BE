package com.haeil.full.contract.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record ContractConditionRequest(
        @NotBlank String conditionDetail, @NotNull @Min(value = 0) BigDecimal amount) {}
