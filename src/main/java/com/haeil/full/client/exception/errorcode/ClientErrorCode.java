package com.haeil.full.client.exception.errorcode;

import com.haeil.full.global.exception.errorcode.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ClientErrorCode implements ErrorCode {
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
