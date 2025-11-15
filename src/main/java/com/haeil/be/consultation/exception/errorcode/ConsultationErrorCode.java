package com.haeil.be.consultation.exception.errorcode;

import com.haeil.be.global.exception.errorcode.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ConsultationErrorCode implements ErrorCode {
    CONSULTATION_RESERVATION_NOT_FOUND(HttpStatus.NOT_FOUND, "상담 예약을 찾을 수 없습니다."),
    CONSULTATION_NOT_FOUND(HttpStatus.NOT_FOUND, "상담을 찾을 수 없습니다."),
    CONSULTATION_NOTE_NOT_FOUND(HttpStatus.NOT_FOUND, "상담 노트를 찾을 수 없습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    CLIENT_NOT_FOUND(HttpStatus.NOT_FOUND, "의뢰인을 찾을 수 없습니다."),
    ASSIGNED_LAWYER_REQUIRED(HttpStatus.BAD_REQUEST, "승인 시 담당 변호사 지정이 필요합니다."),
    INVALID_STATUS_TRANSITION(HttpStatus.BAD_REQUEST, "잘못된 상태 전환입니다."),
    CONSULTATION_NOT_IN_PROGRESS(HttpStatus.BAD_REQUEST, "진행 중인 상담이 아닙니다."),
    CONSULTATION_RESERVATION_NOT_APPROVED(HttpStatus.BAD_REQUEST, "승인된 상담 예약이 아닙니다."),
    UNAUTHORIZED_NOTE_ACCESS(HttpStatus.FORBIDDEN, "상담 노트 수정 권한이 없습니다."),
    INVALID_CONSULTATION_RESERVATION_STATUS_TRANSITION(HttpStatus.BAD_REQUEST, "예약 상태는 PENDING 상태에서만 변경할 수 있습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
