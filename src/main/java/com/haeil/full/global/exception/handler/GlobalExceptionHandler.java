package com.haeil.full.global.exception.handler;

import com.haeil.full.auth.exception.AuthException;
import com.haeil.full.cases.exception.CasesException;
import com.haeil.full.client.exception.ClientException;
import com.haeil.full.consultation.exception.ConsultationException;
import com.haeil.full.contract.exception.ContractException;
import com.haeil.full.global.exception.errorcode.ErrorCode;
import com.haeil.full.global.exception.errorcode.GlobalErrorCode;
import com.haeil.full.global.exception.response.ErrorResponse;
import com.haeil.full.notification.exception.NotificationException;
import com.haeil.full.user.exception.UserException;
import io.micrometer.common.lang.NonNull;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger("ErrorLogger");
    private static final String LOG_FORMAT_INFO = "[🔵INFO] - ({} {}) {} {}: {}";
    private static final String LOG_FORMAT_ERROR = "[🔴ERROR] - ({} {})";

    /**
     * 커스텀 예외 코드 예시 @ExceptionHandler(UserNotFoundException.class) public ResponseEntity<Object>
     * handleMemberNotFound(final UserNotFoundException e) { return
     * handleExceptionInternal(e.getErrorCode()); }
     */
    @ExceptionHandler(AuthException.class)
    public ResponseEntity<Object> handleAuthException(
            final AuthException e, HttpServletRequest request) {
        logInfo(e.getErrorCode(), e, request);
        return handleExceptionInternal(e.getErrorCode());
    }

    @ExceptionHandler(UserException.class)
    public ResponseEntity<Object> handleUserException(
            final UserException e, HttpServletRequest request) {
        logInfo(e.getErrorCode(), e, request);
        return handleExceptionInternal(e.getErrorCode());
    }

    @ExceptionHandler(ClientException.class)
    public ResponseEntity<Object> handleClientException(
            final ClientException e, HttpServletRequest request) {
        logInfo(e.getErrorCode(), e, request);
        return handleExceptionInternal(e.getErrorCode());
    }

    @ExceptionHandler(ConsultationException.class)
    public ResponseEntity<Object> handleConsultationException(
            final ConsultationException e, HttpServletRequest request) {
        logInfo(e.getErrorCode(), e, request);
        return handleExceptionInternal(e.getErrorCode());
    }

    @ExceptionHandler(CasesException.class)
    public ResponseEntity<Object> handleCasesException(
            final CasesException e, HttpServletRequest request) {
        logInfo(e.getErrorCode(), e, request);
        return handleExceptionInternal(e.getErrorCode());
    }

    @ExceptionHandler(ContractException.class)
    public ResponseEntity<Object> handleContractException(
            final ContractException e, HttpServletRequest request) {
        logInfo(e.getErrorCode(), e, request);
        return handleExceptionInternal(e.getErrorCode());
    }

    @ExceptionHandler(NotificationException.class)
    public ResponseEntity<Object> handleNotificationException(
            final NotificationException e, HttpServletRequest request) {
        logInfo(e.getErrorCode(), e, request);
        return handleExceptionInternal(e.getErrorCode());
    }

    /**
     * @Valid 관련 예외 처리 (DTO 검증 실패 시 발생)
     */
    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(
            @NonNull MethodArgumentNotValidException e,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status,
            @NonNull WebRequest request) {
        return handleExceptionInternal(e);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgument(
            IllegalArgumentException e, HttpServletRequest request) {
        logInfo(GlobalErrorCode.INVALID_PARAMETER, e, request);
        return handleExceptionInternal(GlobalErrorCode.INVALID_PARAMETER);
    }

    /** 모든 예외를 처리하는 기본 예외 처리기 */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllException(Exception e, HttpServletRequest request) {
        logError(e, request);
        return handleExceptionInternal(GlobalErrorCode.INTERNAL_SERVER_ERROR);
    }

    /** 예외 처리 결과를 생성하는 내부 메서드 */
    private ResponseEntity<Object> handleExceptionInternal(ErrorCode errorCode) {
        return ResponseEntity.status(errorCode.getHttpStatus()).body(makeErrorResponse(errorCode));
    }

    /** ErrorResponse 객체를 생성하는 메서드 */
    private ErrorResponse makeErrorResponse(ErrorCode errorCode) {
        return ErrorResponse.builder()
                .isSuccess(false)
                .code(errorCode.name())
                .message(errorCode.getMessage())
                .results(new ErrorResponse.ValidationErrors(null))
                .build();
    }

    /** BindException (DTO 검증 실패) 처리 */
    private ResponseEntity<Object> handleExceptionInternal(BindException e) {
        return ResponseEntity.status(GlobalErrorCode.INVALID_PARAMETER.getHttpStatus())
                .body(makeErrorResponse(e));
    }

    /** BindException에서 발생한 유효성 오류를 ErrorResponse로 변환 */
    private ErrorResponse makeErrorResponse(BindException e) {
        final List<ErrorResponse.ValidationError> validationErrorList =
                e.getBindingResult().getFieldErrors().stream()
                        .map(ErrorResponse.ValidationError::from)
                        .toList();

        return ErrorResponse.builder()
                .isSuccess(false)
                .code(GlobalErrorCode.INVALID_PARAMETER.name())
                .message(GlobalErrorCode.INVALID_PARAMETER.getMessage())
                .results(new ErrorResponse.ValidationErrors(validationErrorList))
                .build();
    }

    private void logInfo(ErrorCode ec, Exception e, HttpServletRequest request) {
        log.info(
                LOG_FORMAT_INFO,
                request.getMethod(),
                request.getRequestURI(),
                ec.getHttpStatus(),
                e.getClass().getName(),
                e.getMessage());
    }

    private void logError(Exception e, HttpServletRequest request) {
        log.error(LOG_FORMAT_ERROR, request.getMethod(), request.getRequestURI(), e);
    }
}
