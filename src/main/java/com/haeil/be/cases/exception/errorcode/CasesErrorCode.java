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
    ATTORNEY_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 변호사입니다."),
    PETITION_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이미 소장이 작성된 사건입니다."),
    PETITION_NOT_FOUND(HttpStatus.NOT_FOUND, "소장이 존재하지 않습니다."),
    CASE_DOCUMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "소송문서가 존재하지 않습니다."),
    FILE_REQUIRED(HttpStatus.BAD_REQUEST, "파일이 필요합니다."),
    FILE_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드에 실패했습니다."),
    FILE_DELETE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "파일 삭제에 실패했습니다."),
    INVALID_CASE_STATUS(HttpStatus.BAD_REQUEST, "사건 상태가 유효하지 않습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
