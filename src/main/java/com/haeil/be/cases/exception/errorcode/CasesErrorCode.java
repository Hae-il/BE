package com.haeil.be.cases.exception.errorcode;

import com.haeil.be.global.exception.errorcode.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CasesErrorCode implements ErrorCode {
    CASES_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 사건을 찾을 수 없습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
