package com.haeil.be.global.exception.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import org.springframework.validation.FieldError;

import java.util.List;

@Builder
public record ErrorResponse(
        Boolean isSuccess,
        String code,
        String message,
        @JsonInclude(JsonInclude.Include.NON_EMPTY) // 유효성 오류가 있을 때만 포함됨
        ValidationErrors results) {

    public record ValidationErrors(List<ValidationError> validationErrors){}

    @Builder
    public record ValidationError(String field, String message) {
        public static ValidationError from(final FieldError fieldError) {
            return ValidationError.builder()
                    .field(fieldError.getField())
                    .message(fieldError.getDefaultMessage())
                    .build();
        }
    }
}
