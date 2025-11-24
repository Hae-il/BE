package com.haeil.full.auth.exception.errorcode;

import com.haeil.full.global.exception.errorcode.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode implements ErrorCode {
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),
    INVALID_CREDENTIALS(HttpStatus.BAD_REQUEST, "잘못된 계정 정보입니다."),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
