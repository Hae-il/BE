package com.haeil.be.settlement.exception.errorcode;

import com.haeil.be.global.exception.errorcode.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SettlementErrorCode implements ErrorCode {
    SETTLEMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "정산 정보를 찾을 수 없습니다."),
    INVALID_SETTLEMENT_STATUS(HttpStatus.BAD_REQUEST, "유효하지 않은 정산 상태입니다."),
    INVALID_CALCULATION(HttpStatus.BAD_REQUEST, "정산 금액 계산이 올바르지 않습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
