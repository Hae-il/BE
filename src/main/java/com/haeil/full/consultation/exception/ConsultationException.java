package com.haeil.full.consultation.exception;

import com.haeil.full.global.exception.errorcode.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ConsultationException extends RuntimeException {
    private final ErrorCode errorCode;
}
