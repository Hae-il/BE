package com.haeil.full.contract.exception;

import com.haeil.full.global.exception.errorcode.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ContractException extends RuntimeException {
    private final ErrorCode errorCode;
}
