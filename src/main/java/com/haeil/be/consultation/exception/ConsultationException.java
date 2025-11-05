package com.haeil.be.consultation.exception;

import com.haeil.be.global.exception.errorcode.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ConsultationException extends RuntimeException {
    private final ErrorCode errorCode;
}