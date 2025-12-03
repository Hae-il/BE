package com.haeil.full.contract.dto.request;

import jakarta.validation.constraints.NotNull;

public record ContractStatusUpdateRequest(@NotNull String contractStatus) {}
