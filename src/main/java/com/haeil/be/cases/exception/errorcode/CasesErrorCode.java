package com.haeil.be.cases.exception.errorcode;

import com.haeil.be.global.exception.errorcode.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CasesErrorCode implements ErrorCode {
    CASE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 사건이 존재하지 않습니다."),
    ALREADY_ASSIGNED_CASE(HttpStatus.BAD_REQUEST, "이미 배정된 사건입니다."),
    INVALID_ATTORNEY_ASSIGN(HttpStatus.BAD_REQUEST, "변호사 배정이 불가능한 상태입니다."),
    ATTORNEY_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 변호사입니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
