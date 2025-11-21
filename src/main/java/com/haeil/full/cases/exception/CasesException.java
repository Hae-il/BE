package com.haeil.full.cases.exception;

import com.haeil.full.global.exception.errorcode.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CasesException extends RuntimeException {
    private final ErrorCode errorCode;
}
