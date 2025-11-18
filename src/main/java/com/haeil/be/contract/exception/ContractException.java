package com.haeil.be.contract.exception;

import com.haeil.be.global.exception.errorcode.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ContractException extends RuntimeException {
    private final ErrorCode errorCode;
}
