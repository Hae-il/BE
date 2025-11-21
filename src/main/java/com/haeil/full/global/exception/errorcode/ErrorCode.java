package com.haeil.full.global.exception.errorcode;

import org.springframework.http.HttpStatus;

public interface ErrorCode {

    // enum 클래스 이름을 return
    String name();

    // 할당된 HttpStatus return
    HttpStatus getHttpStatus();

    // 지정된 메시지 반환
    String getMessage();
}
