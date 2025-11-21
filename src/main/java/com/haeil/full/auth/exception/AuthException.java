package com.haeil.full.auth.exception;

import com.haeil.full.global.exception.errorcode.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AuthException extends RuntimeException {
    private final ErrorCode errorCode;
}
