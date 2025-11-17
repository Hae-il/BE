package com.haeil.be.settlement.exception;

import com.haeil.be.global.exception.errorcode.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SettlementException extends RuntimeException {
    private final ErrorCode errorCode;
}
