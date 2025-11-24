package com.haeil.full.client.exception;

import com.haeil.full.global.exception.errorcode.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ClientException extends RuntimeException {
    private final ErrorCode errorCode;
}
