package com.haeil.be.contract.exception.errorcode;

import com.haeil.be.global.exception.errorcode.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ContractErrorCode implements ErrorCode {
    INVALID_FEE_TYPE(HttpStatus.BAD_REQUEST,"지원하지 않는 계약유형입니다."),
    CONTRACT_NOT_FOUND(HttpStatus.NOT_FOUND,"수임 계약이 존재하지 않습니다."),
    CONDITION_SHOULD_NOT_BE_NULL(HttpStatus.BAD_REQUEST,"정액 계약은 보수 조건 목록이 비어있을 수 없습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
