package com.haeil.full.settlement.exception;

import com.haeil.full.global.exception.errorcode.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SettlementException extends RuntimeException {
    private final ErrorCode errorCode;
}
