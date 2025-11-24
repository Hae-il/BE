package com.haeil.be.contract.dto.request;

import com.haeil.be.contract.domain.type.ContractStatus;
import com.haeil.be.global.validation.ValidEnum;

public record ContractStatusUpdateRequest(
        @ValidEnum(enumClass = ContractStatus.class) String contractStatus) {}
