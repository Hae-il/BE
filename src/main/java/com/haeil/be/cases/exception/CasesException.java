package com.haeil.be.cases.exception;

import com.haeil.be.global.exception.errorcode.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CasesException extends RuntimeException {
    private final ErrorCode errorCode;
}
