package com.haeil.be.contract.exception.errorcode;

import com.haeil.be.global.exception.errorcode.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ContractErrorCode implements ErrorCode {
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
